package game.update;

import main.Manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Update {

    private static ArrayList<OnUpdate> onUpdates = new ArrayList<OnUpdate>();
    private static ArrayList<OnUpdate> onPhysicsUpdates = new ArrayList<OnUpdate>();
    private static ArrayList<OnUpdate> onRenderUpdates = new ArrayList<OnUpdate>();
    private static ArrayList<OnUpdate> onLateUpdates = new ArrayList<OnUpdate>();

    public static boolean pausePhysics = false;

    public static void init() {
        Thread updateThread = new Thread(() -> {
            while (Manager.applicationRunning) {
                for (var ou: onUpdates) {
                    ou.onUpdate();
                }
                if (!pausePhysics) {
                    for (var ou : onPhysicsUpdates) {
                        ou.onUpdate();
                    }
                }
                for (var ou: onRenderUpdates) {
                    ou.onUpdate();
                }
                for (var ou: onLateUpdates) {
                    ou.onUpdate();
                }

                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    public static void addUpdate(OnUpdate onUpdate) {
        onUpdates.add(onUpdate);
    }

    public static void addPhysicsUpdate(OnUpdate onPhysicsUpdate) {
        onPhysicsUpdates.add(onPhysicsUpdate);
    }

    public static void removePhysicsUpdate(OnUpdate onPhysicsUpdate) {
        onPhysicsUpdates.remove(onPhysicsUpdate);
    }

    public static void addRenderUpdate(OnUpdate onRenderUpdate) {
        onRenderUpdates.add(onRenderUpdate);
    }

    public static void addLateUpdate(OnUpdate onLateUpdate) {
        onLateUpdates.add(onLateUpdate);
    }

}
