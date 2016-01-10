package scripting;

import client.MapleClient;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import server.quest.MapleQuest;
import tools.FileoutputUtil;

public class NPCScriptManager extends AbstractScriptManager {

    private final Map<MapleClient, NPCConversationManager> cms = new WeakHashMap<>();
    private static final NPCScriptManager instance = new NPCScriptManager();

    public static final NPCScriptManager getInstance() {
        return instance;
    }

    public final void start(final MapleClient c, final int npc) {
        start(c, npc, null);
    }

    public final void start(final MapleClient c, final int npc, String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("NPC腳本", false, "開啟對話，NPC：" + npc + " 特殊腳本：" + script + c.getPlayer().getMap());
            }
            if (!cms.containsKey(c) && c.canClickNPC()) {
                Invocable iv;
                if (script == null) {
                    iv = getInvocable("NPC/" + npc + ".js", c, true); //safe disposal
                } else {
                    iv = getInvocable("特殊/" + script + ".js", c, true); //safe disposal
                }
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("NPC腳本", true, "找不到NPCID:" + npc + " 特殊腳本:" + script + c.getPlayer().getMap());
                    }
                    System.out.println("\r\n找不到NPCID:" + npc + " 特殊腳本:" + script + c.getPlayer().getMap() + "\r\n");
                    iv = getInvocable("特殊/notcoded.js", c, true); //safe disposal
                    if (iv == null) {
                        dispose(c);
                        return;
                    }
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, -1, script, ScriptType.NPC, iv);
                cms.put(c, cm);
                scriptengine.put("cm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                try {
                    iv.invokeFunction("start"); // Temporary until I've removed all of start
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            } else {
                if (c.getPlayer().isShowErr()) {
                    c.getPlayer().showInfo("NPC腳本", true, "無法執行腳本:已有腳本執行-" + cms.containsKey(c) + " | 允許執行腳本-" + c.canClickNPC());
                }
                c.getPlayer().dropMessage(-1, "你當前已經和1個NPC對話了. 如果不是請輸入 @解卡 指令進行解卡。");
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("執行NPC腳本出錯 : NPC - " + npc + " 特殊腳本 - " + script + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行NPC腳本出錯, NPC - " + npc + " 特殊腳本 - " + script + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, npc, script, ScriptType.NPC);
        } finally {
            lock.unlock();
        }
    }

    public final void action(final MapleClient c, final byte mode, final byte type, final int selection) {
        if (mode != -1) {
            final NPCConversationManager cm = cms.get(c);
            if (cm == null || cm.getLastMsg() > -1) {
                return;
            }
            final Lock lock = c.getNPCLock();
            lock.lock();
            try {
                if (cm.pendingDisposal) {
                    dispose(c);
                } else {
                    c.setClickedNPC();
//                    System.err.println("mode " + mode);
//                    System.err.println("type " + type);
//                    System.err.println("sel " + selection);
                    cm.getIv().invokeFunction("action", mode, type, selection);
                }
            } catch (final ScriptException | NoSuchMethodException e) {
                String str;
                switch (cm.getType()) {
                    case NPC:
                        str = "執行NPC腳本出錯 : NPC - " + cm.getNpc() + " 特殊腳本 - " + cm.getScript();
                        break;
                    case ITEM:
                        str = "執行道具腳本出錯 : NPC - " + cm.getNpc() + " 腳本 - " + cm.getScript();
                        break;
                    case ON_USER_ENTER:
                        str = "執行地圖onUserEnter腳本出錯 : 腳本 - " + cm.getScript();
                        break;
                    case ON_FIRST_USER_ENTER:
                        str = "執行地圖onFirstUserEnter腳本出錯 : 腳本 - " + cm.getScript();
                        break;
                    default:
                        str = "執行腳本出錯 : NPC - " + cm.getNpc() + " 腳本 - " + cm.getScript();
                }
                System.err.println(str + " ." + e);
                FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n" + str + " ." + e + "\r\n\r\n");
                dispose(c);
                notice(c, cm.getNpc(), cm.getScript(), cm.getType());
            } finally {
                lock.unlock();
            }
        }
    }

    public final void startQuest(final MapleClient c, final int npc, final int quest) {
        if (!MapleQuest.getInstance(quest).canStart(c.getPlayer(), null)) {
            if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("任務開始腳本", true, "無法開始任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest));
            }
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("任務開始腳本", false, "腳本 - 開始任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest));
            }
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("任務/" + quest + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("任務開始腳本", true, "找不到腳本任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest) + c.getPlayer().getMap());
                    }
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, null, ScriptType.QUEST_START, iv);
                cms.put(c, cm);
                scriptengine.put("qm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                iv.invokeFunction("start", (byte) 1, (byte) 0, 0); // start it off as something
            } else if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("任務開始腳本", true, "無法執行腳本:已有腳本執行-" + cms.containsKey(c) + " | 允許執行腳本-" + c.canClickNPC());
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("執行任務開始腳本出錯 : NPC - " + npc + "任務 - " + MapleQuest.getInstance(quest) + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行任務開始腳本出錯 : NPC - " + npc + "任務 - " + MapleQuest.getInstance(quest) + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, npc, String.valueOf(quest), ScriptType.QUEST_START);
        } finally {
            lock.unlock();
        }
    }

    public final void startQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                c.setClickedNPC();
                cm.getIv().invokeFunction("start", mode, type, selection);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("執行任務開始腳本出錯 : NPC - " + cm.getNpc() + "任務 - " + MapleQuest.getInstance(cm.getQuest()) + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行任務開始腳本出錯 : NPC - " + cm.getNpc() + "任務 - " + MapleQuest.getInstance(cm.getQuest()) + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, cm.getNpc(), String.valueOf(cm.getQuest()), cm.getType());
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final int npc, final int quest, final boolean customEnd) {
        if (!customEnd && !MapleQuest.getInstance(quest).canComplete(c.getPlayer(), null)) {
            if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("任務完成腳本", true, "無法完成任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest));
            }
            return;
        }
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("任務完成腳本", false, "腳本 - 完成任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest));
            }
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("任務/" + quest + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("任務完成腳本", true, "找不到腳本任務 NPC：" + npc + " 任務：" + MapleQuest.getInstance(quest) + c.getPlayer().getMap());
                    }
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, quest, null, ScriptType.QUEST_END, iv);
                cms.put(c, cm);
                scriptengine.put("qm", cm);

                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                iv.invokeFunction("end", (byte) 1, (byte) 0, 0); // start it off as something
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("執行任務完成腳本出錯 : NPC - " + npc + "任務 - " + MapleQuest.getInstance(quest) + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行任務完成腳本出錯 : NPC - " + npc + "任務 - " + MapleQuest.getInstance(quest) + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, npc, String.valueOf(quest), ScriptType.QUEST_END);
        } finally {
            lock.unlock();
        }
    }

    public final void endQuest(final MapleClient c, final byte mode, final byte type, final int selection) {
        final Lock lock = c.getNPCLock();
        final NPCConversationManager cm = cms.get(c);
        if (cm == null || cm.getLastMsg() > -1) {
            return;
        }
        lock.lock();
        try {
            if (cm.pendingDisposal) {
                dispose(c);
            } else {
                c.setClickedNPC();
                cm.getIv().invokeFunction("end", mode, type, selection);
            }
        } catch (ScriptException | NoSuchMethodException e) {
            System.err.println("執行任務完成腳本出錯 : NPC - " + cm.getNpc() + "任務 - " + MapleQuest.getInstance(cm.getQuest()) + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行任務完成腳本出錯 : NPC - " + cm.getNpc() + "任務 - " + MapleQuest.getInstance(cm.getQuest()) + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, cm.getNpc(), String.valueOf(cm.getQuest()), cm.getType());
        } finally {
            lock.unlock();
        }
    }

    public final void startItemScript(final MapleClient c, final int npc, final String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("道具腳本", false, "開啟對話，NPC：" + npc + " 道具腳本：" + script);
            }
            if (!cms.containsKey(c) && c.canClickNPC()) {
                final Invocable iv = getInvocable("道具/" + script + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().dropMessage(5, "找不到道具腳本:" + script);
                        c.getPlayer().showInfo("道具腳本", true, "找不到道具腳本:" + script);
                    }
                    System.out.println("\r\n找不到道具腳本:" + script + "\r\n");
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, npc, -1, script, ScriptType.ITEM, iv);
                cms.put(c, cm);
                scriptengine.put("im", cm);
                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                try {
                    iv.invokeFunction("start");
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            } else if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("道具腳本", true, "無法執行腳本:已有腳本執行-" + cms.containsKey(c) + " | 允許執行腳本-" + c.canClickNPC());
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("執行道具腳本出錯 : NPC - " + npc + " 腳本 - " + script + " ." + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "\r\n\r\n執行道具腳本出錯, NPC - " + npc + " 腳本 - " + script + " ." + e + "\r\n\r\n");
            dispose(c);
            notice(c, npc, script, ScriptType.ITEM);
        } finally {
            lock.unlock();
        }
    }

    public final void onUserEnter(final MapleClient c, final String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("onUserEnter腳本", false, "開始onUserEnter腳本：" + script + c.getPlayer().getMap());
            }
            if (!cms.containsKey(c)) {
                final Invocable iv = getInvocable("地圖/onUserEnter/" + script + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("onUserEnter腳本", true, "找不到onUserEnter腳本:" + script + c.getPlayer().getMap());
                    }
                    System.out.println("\r\n找不到onUserEnter腳本:" + script + c.getPlayer().getMap() + "\r\n");
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, 0, -1, script, ScriptType.ON_USER_ENTER, iv);
                cms.put(c, cm);
                scriptengine.put("ms", cm);
                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                try {
                    iv.invokeFunction("start");
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            } else if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("onUserEnter腳本", true, "無法執行腳本:已有腳本執行-" + cms.containsKey(c));
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("執行onUserEnter腳本出錯 : " + script + ". " + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "執行onUserEnter腳本出錯 : " + script + ". " + e);
            dispose(c);
            notice(c, 0, script, ScriptType.ON_USER_ENTER);
        } finally {
            lock.unlock();
        }
    }

    public final void onFirstUserEnter(final MapleClient c, final String script) {
        final Lock lock = c.getNPCLock();
        lock.lock();
        try {
            if (c.getPlayer().isShowInfo()) {
                c.getPlayer().showInfo("onFirstUserEnter腳本", false, "開始onFirstUserEnter腳本：" + script + c.getPlayer().getMap());
            }
            if (!cms.containsKey(c)) {
                final Invocable iv = getInvocable("地圖/onFirstUserEnter/" + script + ".js", c, true);
                if (iv == null) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("onFirstUserEnter腳本", true, "找不到onFirstUserEnter腳本:" + script + c.getPlayer().getMap());
                    }
                    System.out.println("\r\n找不到onFirstUserEnter腳本:" + script + c.getPlayer().getMap() + "\r\n");
                    dispose(c);
                    return;
                }
                final ScriptEngine scriptengine = (ScriptEngine) iv;
                final NPCConversationManager cm = new NPCConversationManager(c, 0, -1, script, ScriptType.ON_FIRST_USER_ENTER, iv);
                cms.put(c, cm);
                scriptengine.put("ms", cm);
                c.getPlayer().setConversation(1);
                c.setClickedNPC();
                try {
                    iv.invokeFunction("start");
                } catch (NoSuchMethodException nsme) {
                    iv.invokeFunction("action", (byte) 1, (byte) 0, 0);
                }
            } else if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("onFirstUserEnter腳本", true, "無法執行腳本:已有腳本執行-" + cms.containsKey(c));
            }
        } catch (final ScriptException | NoSuchMethodException e) {
            System.err.println("執行地圖onFirstUserEnter腳本出錯 : " + script + ". " + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "執行地圖onFirstUserEnter腳本出錯 : " + script + ". " + e);
            dispose(c);
            notice(c, 0, script, ScriptType.ON_FIRST_USER_ENTER);
        } finally {
            lock.unlock();
        }
    }

    public final void dispose(final MapleClient c) {
        final NPCConversationManager npccm = cms.get(c);
        if (npccm != null) {
            cms.remove(c);
            switch (npccm.getType()) {
                case NPC:
                    c.removeScriptEngine("腳本/NPC/" + npccm.getNpc() + ".js");
                    c.removeScriptEngine("腳本/特殊/" + npccm.getScript() + ".js");
                    c.removeScriptEngine("腳本/特殊/notcoded.js");
                    break;
                case ITEM:
                    c.removeScriptEngine("腳本/道具/" + npccm.getScript() + ".js");
                    break;
                case ON_USER_ENTER:
                    c.removeScriptEngine("腳本/地圖/onUserEnter/" + npccm.getScript() + ".js");
                    break;
                case ON_FIRST_USER_ENTER:
                    c.removeScriptEngine("腳本/地圖/onFirstUserEnter/" + npccm.getScript() + ".js");
                    break;
                default:
                    c.removeScriptEngine("腳本/任務/" + npccm.getQuest() + ".js");
            }
        }
        if (c.getPlayer() != null && c.getPlayer().getConversation() == 1) {
            c.getPlayer().setConversation(0);
        }
    }

    public final NPCConversationManager getCM(final MapleClient c) {
        return cms.get(c);
    }

    private void notice(MapleClient c, int npcId, String script, ScriptType type) {
        String str;
        switch (type) {
            case NPC:
                str = "NPC腳本出錯-NPC:" + npcId + (script != null ? " 特殊腳本:" + script : "");
                break;
            case ITEM:
                str = "道具腳本出錯-NPC:" + npcId + " 腳本:" + script;
                break;
            case ON_USER_ENTER:
                str = "onUserEnter腳本出錯-腳本:" + script;
                break;
            case ON_FIRST_USER_ENTER:
                str = "onFirstUserEnter腳本出錯-腳本:" + script;
                break;
            default:
                str = "腳本出錯-NPC:" + npcId + " 腳本:" + script;
        }
        c.getPlayer().dropMessage(1, str);
    }
}
