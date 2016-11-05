package mir2.robot;

import mir2.role.GameRole;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2Robot {
    private static GameRole role = GameRole.getInstance();

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        new Thread(new HpMpDetectRunnable()).start();//实时监控HP MP
        while (true){
            System.out.println("hp: " + role.getHp() + "    mp: " + role.getMp());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
