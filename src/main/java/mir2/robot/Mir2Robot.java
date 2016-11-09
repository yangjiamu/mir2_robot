package mir2.robot;

import mir2.role.GameRole;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2Robot {
    private static GameRole role = new GameRole();

    public static void main(String[] args) {
        role.setCareer("战士");
        role.setLevel(24);
        role.setHp(295);
        role.setCurrentHp(295);
        role.setMp(95);
        role.setCurrentMp(95);
        start();
    }

    public static void start() {
        new Thread(new HpMpDetectionRunnable()).start();//实时监控HP MP
        new Thread(new CoordinationDetectionRunnable(new GameRole())).start();//实时监控坐标
        while (true){
            System.out.println("hp: " + role.getCurrentHp() + "    mp: " + role.getCurrentMp());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
