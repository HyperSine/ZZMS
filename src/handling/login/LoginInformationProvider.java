package handling.login;

import client.MapleJob;
import constants.GameConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Triple;

public class LoginInformationProvider {

    public enum JobType {

        終極冒險家(-1, MapleJob.初心者.getId(), 100000000, false, false, true, false, false, false, false),
        末日反抗軍(0, MapleJob.市民.getId(), 931000000, false, false, false, false, false, false, false),
        冒險家(1, MapleJob.初心者.getId(), 4000000, false, false, false, false, false, false, false),
        皇家騎士團(2, MapleJob.貴族.getId(), 130030000, false, false, false, true, false, false, false),
        狂狼勇士(3, MapleJob.傳說.getId(), 914000000, false, false, true, false, false, false, false),
        龍魔導士(4, MapleJob.龍魔導士.getId(), 900010000, false, false, true, false, false, false, false),
        精靈遊俠(5, MapleJob.精靈遊俠.getId(), 910150000, false, false, false, false, false, false, false),//TODO 出生劇情101050000 - 910150000
        惡魔(6, MapleJob.惡魔殺手.getId(), 931050310, true, false, false, false, false, false, false),
        幻影俠盜(7, MapleJob.幻影俠盜.getId(), 915000000, false, false, false, true, false, false, false),//TODO 出生劇情
        影武者(8, MapleJob.初心者.getId(), 103050900, false, false, false, false, false, false, false),
        米哈逸(9, MapleJob.米哈逸.getId(), 913070000, false, false, true, false, false, false, false),//TODO 出生劇情修正
        夜光(10, MapleJob.夜光.getId(), 101000000, false, false, false, true, false, false, false),//TODO 出生劇情Ellinia atm
        凱撒(11, MapleJob.凱撒.getId(), 400000000, false, false, false, false, false, false, false),//TODO 出生劇情
        天使破壞者(12, MapleJob.天使破壞者.getId(), 940011000, false, false, false, false, false, false, false),//TODO 出生劇情400000000 - 940011000 - town now
        重砲指揮官(13, MapleJob.初心者.getId(), 0, false, false, false, false, false, false, false),//TODO 出生劇情
        傑諾(14, MapleJob.傑諾.getId(), 931050920, true, false, false, false, false, false, false),//TODO 出生劇情
        神之子(15, MapleJob.神之子.getId(), 100000000, false, false, false, true, false, false, false),//TODO 出生劇情321000000 = zero starter map
        隱月(16, MapleJob.隱月.getId(), 910000000, false, false, true, true, false, false, false),//TODO 出生劇情
        皮卡啾(17, MapleJob.皮卡啾.getId(), 100000000, false, false, false, false, false, false, false),//TODO 出生劇情
//        凱內西斯(17, MapleJob.凱內西斯.getId(), 100000000, false, false, false, false, false, false, false),//TODO 出生劇情
        蒼龍俠客(18, MapleJob.初心者.getId(), 552000050, false, false, true, false, false, false, false),//TODO 出生劇情End map for tutorial
        劍豪(19, MapleJob.劍豪.getId(), 807100010, false, true, false, false, false, false, true),
        陰陽師(20, MapleJob.陰陽師.getId(), 807100110, false, true, false, false, false, false, true),//TODO 出生劇情
        幻獸師(21, MapleJob.幻獸師.getId(), 866135000, true, false, false, false, true, true, false);
        public int type, id, map;
        public boolean faceMark, hat, bottom, cape, ears, tail, glove;

        private JobType(int type, int id, int map, boolean faceMark, boolean hat, boolean bottom, boolean cape, boolean ears, boolean tail, boolean glove) {
            this.type = type;
            this.id = id;
            this.map = map;
            this.faceMark = faceMark;
            this.hat = hat;
            this.bottom = bottom;
            this.cape = cape;
            this.ears = ears;
            this.tail = tail;
            this.glove = glove;
        }
        
        public static JobType getByType(int g) {
            if (g == JobType.重砲指揮官.type) {
                return JobType.冒險家;
            }
            for (JobType e : JobType.values()) {
                if (e.type == g) {
                    return e;
                }
            }
            return null;
        }

        public static JobType getById(int g) {
            if (g == JobType.冒險家.id) {
                return JobType.冒險家;
            }
            if (g == 508) {
                return JobType.蒼龍俠客;
            }
            for (JobType e : JobType.values()) {
                if (e.id == g) {
                    return e;
                }
            }
            return null;
        }
    }
    private final static LoginInformationProvider instance = new LoginInformationProvider();
    protected final List<String> ForbiddenName = new ArrayList<>();
    //gender, val, job
    protected final Map<Triple<Integer, Integer, Integer>, List<Integer>> makeCharInfo = new HashMap<>();
    //0 = eyes 1 = hair 2 = haircolor 3 = skin 4 = top 5 = bottom 6 = shoes 7 = weapon

    public static LoginInformationProvider getInstance() {
        return instance;
    }

    protected LoginInformationProvider() {
        final MapleDataProvider prov = MapleDataProviderFactory.getDataProvider("/Etc.wz");
        MapleData nameData = prov.getData("ForbiddenName.img");
        for (final MapleData data : nameData.getChildren()) {
            ForbiddenName.add(MapleDataTool.getString(data));
        }
        nameData = prov.getData("Curse.img");
        for (final MapleData data : nameData.getChildren()) {
            ForbiddenName.add(MapleDataTool.getString(data).split(",")[0]);
        }
        final MapleData infoData = prov.getData("MakeCharInfo.img");
        final MapleData data = infoData.getChildByPath("Info");
        for (MapleData dat : infoData) {
            try {
                final int type;
                if (dat.getName().equals("000_1")) {
                    type = JobType.影武者.type;
                } else {
                    type = JobType.getById(Integer.parseInt(dat.getName())).type;
                }
                for (MapleData d : dat) {
                    int val;
                    if (d.getName().contains("female")) {
                        val = 1;
                    } else if (d.getName().contains("male")) {
                        val = 0;
                    } else {
                        continue;
                    }
                    for (MapleData da : d) {
                        int index;
                        Triple<Integer, Integer, Integer> key;
                        index = Integer.parseInt(da.getName());
                        key = new Triple<>(val, index, type);
                        List<Integer> our = makeCharInfo.get(key);
                        if (our == null) {
                            our = new ArrayList<>();
                            makeCharInfo.put(key, our);
                        }
                        for (MapleData dd : da) {
                            if (dd.getName().equalsIgnoreCase("color")) {
                                for (MapleData dda : dd) {
                                    for (MapleData ddd : dda) {
                                        our.add(MapleDataTool.getInt(ddd, -1));
                                    }
                                }
                            } else {
                                try {
                                    our.add(MapleDataTool.getInt(dd, -1));
                                } catch (Exception ex) { //probably like color
                                    for (MapleData dda : dd) {
                                        for (MapleData ddd : dda) {
                                            our.add(MapleDataTool.getInt(ddd, -1));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (NumberFormatException | NullPointerException e) {
            }
        }
        final MapleData uA = infoData.getChildByPath("UltimateAdventurer");
        for (MapleData dat : uA) {
            final Triple<Integer, Integer, Integer> key = new Triple<>(-1, Integer.parseInt(dat.getName()), JobType.終極冒險家.type);
            List<Integer> our = makeCharInfo.get(key);
            if (our == null) {
                our = new ArrayList<>();
                makeCharInfo.put(key, our);
            }
            for (MapleData d : dat) {
                our.add(MapleDataTool.getInt(d, -1));
            }
        }
    }

    public static boolean isExtendedSpJob(int jobId) {
        return GameConstants.isSeparatedSp(jobId);
    }

    public final boolean isForbiddenName(final String in) {
        for (final String name : ForbiddenName) {
            if (in.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public final boolean isEligibleItem(final int gender, final int val, final int job, final int item) {
        if (item < 0) {
            return false;
        }
        final Triple<Integer, Integer, Integer> key = new Triple<>(gender, val, job);
        final List<Integer> our = makeCharInfo.get(key);
        if (our == null) {
            return false;
        }
        return our.contains(item);
    }
}
