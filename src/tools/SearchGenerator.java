/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import client.MapleJob;
import client.Skill;
import client.SkillFactory;
import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import provider.MapleData;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.ItemInformation;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.quest.MapleQuest;

/**
 *
 * @author Pungin
 */
public class SearchGenerator {

    public enum SearchType {

        道具(1),
        NPC(2),
        地圖(3),
        怪物(4),
        任務(5),
        技能(6),
        職業(7),
        伺服器包頭(8),
        用戶端包頭(9),
        髮型(10),
        臉型(11),
        未知;

        private int value;

        SearchType() {
            this.value = 0;
        }

        SearchType(int value) {
            this.value = value;
        }

        public final int getValue() {
            return value;
        }

        public static String nameOf(int value) {
            for (SearchType type : SearchType.values()) {
                if (type.getValue() == value) {
                    return type.name();
                }
            }
            return "未知";
        }
    }
    public static final int 道具 = SearchType.道具.getValue();
    public static final int NPC = SearchType.NPC.getValue();
    public static final int 地圖 = SearchType.地圖.getValue();
    public static final int 怪物 = SearchType.怪物.getValue();
    public static final int 任務 = SearchType.任務.getValue();
    public static final int 技能 = SearchType.技能.getValue();
    public static final int 職業 = SearchType.職業.getValue();
    public static final int 伺服器包頭 = SearchType.伺服器包頭.getValue();
    public static final int 用戶端包頭 = SearchType.用戶端包頭.getValue();
    public static final int 髮型 = SearchType.髮型.getValue();
    public static final int 臉型 = SearchType.臉型.getValue();
    private static final Map<SearchType, Map<Integer, String>> searchs = new HashMap();

    public static Map<Integer, String> getSearchs(int type) {
        return getSearchs(SearchType.valueOf(SearchType.nameOf(type)));
    }

    public static Map<Integer, String> getSearchs(SearchType type) {
        if (searchs.containsKey(type)) {
            return searchs.get(type);
        }

        Map<Integer, String> values = new TreeMap<>((v1, v2) -> v1.compareTo(v2));

        switch (type) {
            case 道具:
                for (ItemInformation itemInfo : MapleItemInformationProvider.getInstance().getAllItems()) {
                    values.put(itemInfo.itemId, itemInfo.name);
                }
                break;
            case NPC:
                values = MapleLifeFactory.getNPCNames();
                break;
            case 地圖:
                MapleData data = MapleDataProviderFactory.getDataProvider("String.wz").getData("Map.img");
                for (MapleData mapAreaData : data.getChildren()) {
                    for (MapleData mapIdData : mapAreaData.getChildren()) {
                        values.put(Integer.parseInt(mapIdData.getName()), "'" + MapleDataTool.getString(mapIdData.getChildByPath("streetName"), "無名稱") + " : " + MapleDataTool.getString(mapIdData.getChildByPath("mapName"), "無名稱") + "'");
                    }
                }
                break;
            case 怪物:
                for (Map.Entry<Integer, String> mob : MapleMonsterInformationProvider.getInstance().getAllMonsters().entrySet()) {
                    values.put(mob.getKey(), mob.getValue());
                }
                break;
            case 任務:
                for (MapleQuest quest : MapleQuest.getAllInstances()) {
                    values.put(quest.getId(), quest.getName());
                }
                break;
            case 技能: {
                for (Skill skill : SkillFactory.getAllSkills()) {
                    values.put(skill.getId(), skill.getName());
                }
                break;
            }
            case 職業:
                for (MapleJob job : MapleJob.values()) {
                    values.put(job.getId(), job.name());
                }
                break;
            case 伺服器包頭:
                for (SendPacketOpcode send : SendPacketOpcode.values()) {
                    values.put((int) send.getValue(), send.name());
                }
                break;
            case 用戶端包頭:
                for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
                    values.put((int) recv.getValue(), recv.name());
                }
                break;
            case 髮型:
                values = MapleItemInformationProvider.getInstance().getHairList();
                break;
            case 臉型:
                values = MapleItemInformationProvider.getInstance().getFaceList();
                break;
        }

        searchs.put(type, values);
        return values;
    }

    public static Map<Integer, String> getSearchData(int type, String search) {
        return getSearchData(SearchType.valueOf(SearchType.nameOf(type)), search);
    }

    public static Map<Integer, String> getSearchData(SearchType type, String search) {
        Map<Integer, String> values = new TreeMap<>((v1, v2) -> v1.compareTo(v2));
        Map<Integer, String> ss = getSearchs(type);

        for (int i : ss.keySet()) {
            if (String.valueOf(i).toLowerCase().contains(search.toLowerCase()) || ss.get(i).toLowerCase().contains(search.toLowerCase())) {
                values.put(i, ss.get(i));
            }
        }

        return values;
    }

    public static String searchData(int type, String search) {
        return searchData(SearchType.valueOf(SearchType.nameOf(type)), search);
    }

    public static String searchData(SearchType type, String search) {
        Map<Integer, String> ss = getSearchData(type, search);
        List<String> ret = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        switch (type) {
            case 道具:
                ss.keySet().forEach((i) -> ret.add("\r\n#L" + i + "##i" + i + ":# #z" + i + "#(" + i + ")#l"));
                break;
            case NPC:
                ss.entrySet().forEach((i) -> ret.add("\r\n#L" + i.getKey() + "##p" + i.getKey() + "##" + i.getValue() + "(" + i.getKey() + ")#l"));
                break;
            case 地圖:
                ss.keySet().forEach((i) -> ret.add("\r\n#L" + i + "##m" + i + "#(" + i + ")#l"));
                break;
            case 怪物:
                ss.keySet().forEach((i) -> ret.add("\r\n#L" + i + "##o" + i + "#(" + i + ")#l"));
                break;
            case 任務:
                ss.entrySet().forEach((i) -> ret.add("\r\n#L" + i.getKey() + "#" + i.getValue() + "(" + i.getKey() + ")#l"));
                break;
            case 技能:
                ss.entrySet().forEach((i) -> ret.add("\r\n#L" + i.getKey() + "##s" + i.getKey() + "#" + i.getValue() + "(" + i.getKey() + ")#l"));
                break;
            case 職業:
                ss.entrySet().forEach((i) -> ret.add("\r\n#L" + i.getKey() + "#" + i.getValue() + "(" + i.getKey() + ")#l"));
                break;
            case 伺服器包頭:
            case 用戶端包頭:
                ss.entrySet().forEach((i) -> ret.add("\r\n" + i.getValue() + " 值: " + i.getKey() + " 16進制: " + HexTool.getOpcodeToString(i.getKey())));
                break;
            default:
                sb.append("對不起, 這個檢索類型不被支援");
        }

        if (ret.size() > 0) {
            for (String singleRetItem : ret) {
                if (sb.length() > 3500) {
                    sb.append("\r\n後面還有很多搜尋結果, 但已經無法顯示更多");
                    break;
                }
                sb.append(singleRetItem);
            }
        }

        StringBuilder sbs = new StringBuilder();
        if (!sb.toString().isEmpty() && !sb.toString().equalsIgnoreCase("對不起, 這個檢索指令不被支援")) {
            sbs.append("<<類型: ").append(type.name()).append(" | 搜尋訊息: ").append(search).append(">>");
        }
        sbs.append(sb);
        if (sbs.toString().isEmpty()) {
            sbs.append("搜尋不到此").append(type.name());
        }
        return sbs.toString();
    }

    public static boolean foundData(int type, String search) {
        return !getSearchData(type, search).isEmpty();
    }
}
