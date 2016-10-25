package mir2;

import com.xnx3.microsoft.Com;
import com.xnx3.microsoft.Window;
import com.xnx3.robot.Robot;

import java.awt.event.KeyEvent;
/**
 * Created by yangwenjie on 16/10/24.
 */
public class FirstMir2 {
    private static final Robot robot = new Robot();
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000*10);
        Com com = new Com();
        if(!com.isCreateSuccess()){
            System.out.println("create com error");
            return;
        }

        Window window=new Window(com.getActiveXComponent());
        //查找当前运行的程序中标题包含“米尔”三个字的窗口句柄，
        int hwnd=window.findWindow(0, null, "Mir");
        //如果找到了寻仙这个游戏窗口了，确定是有这个程序存在，那么可以继续以下操作
        if(hwnd>0) {
			/*
			 * 对找到的寻仙这个窗口进行绑定，绑定完毕后，那么所有的鼠标、键盘、图色等操作就是都是对这个窗口（寻仙游戏）操作的，完全后台的~~，窗口可以被遮挡、点击葫芦隐藏，但是不可以最小化
			 * 寻仙的就是这种模式绑定，只需要传入窗口句柄就可
			 * 其他游戏的请自行组合测试绑定模式,使用： com.bind(hwnd, display, mouse, key, mode) 自行测试
			 */
            System.out.println("11111");
            if (com.bind(hwnd)) {
                /**
                 * 绑定完毕，对游戏的操作全在这里
                 */
                System.out.println("2222222");
                say("Hello, I am ou yang");
            }
        }
    }

    public static void say(String str){
        robot.press((int) KeyEvent.KEY_EVENT_MASK);
        for (int i = 0; i < str.length(); i++) {
            robot.press(robot.StringToKey(str.substring(i, i+1)));
        }
        robot.press((int) KeyEvent.KEY_EVENT_MASK);
    }
}
