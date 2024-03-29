package me.spoony.botanico.client;

import me.spoony.botanico.Botanico;
import me.spoony.botanico.client.engine.Window;
import me.spoony.botanico.client.graphics.RendererGUI;
import me.spoony.botanico.client.input.*;
import me.spoony.botanico.client.input.Input;
import me.spoony.botanico.client.views.GameView;
import me.spoony.botanico.client.views.LoadingView;
import me.spoony.botanico.client.views.IView;
import me.spoony.botanico.client.views.menu.MenuView;
import me.spoony.botanico.common.buildings.Building;
import me.spoony.botanico.common.crafting.Recipes;
import me.spoony.botanico.common.items.Item;
import me.spoony.botanico.common.net.Packets;
import me.spoony.botanico.common.tiles.Tile;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

public final class BotanicoGame {

  public static Window WINDOW;

  public static boolean RUNNING;
  private static long LAST_LOOP_TIME;
  public static double DELTA;
  public static double FPS;

  private static IView nextView;
  private static IView view;
  private static final ResourceManager RESOURCE_MANAGER;

  public static long getLastLoopTime() {
    return LAST_LOOP_TIME;
  }

  static {
    RESOURCE_MANAGER = new ResourceManager();
  }

  public static IView getView() {
    synchronized (view) {
      return view;
    }
  }

  public static void setView(IView view) {
    System.out.println("Changing View to " + view.getClass().getSimpleName());
    nextView = view;
  }

  public static void start() {
    System.out.println("Creating window");
    BotanicoGame.WINDOW = new Window(960, 640, "Botanico");
    BotanicoGame.WINDOW.init();
    System.out.println("Showing window");
    BotanicoGame.WINDOW.show();

    BotanicoGame.setView(new LoadingView());

    //Config.init("assets/config/");

    // INITIALIZE REGISTRIES
    System.out.println("Initializing registries");
    Input.init();
    Packets.init();
    Tile.initRegistry();
    Building.initRegistry();
    Item.initRegistry();
    Recipes.init();

    try {
      GLFWErrorCallback.createPrint(System.err).set();

      System.out.println("Starting render loop");
      BotanicoGame.run();

      System.out.println("Shutting down!");
      BotanicoGame.getView().unloadContent();

      Callbacks.glfwFreeCallbacks(WINDOW.getHandle());
      GLFW.glfwDestroyWindow(WINDOW.getHandle());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      GLFW.glfwTerminate();
      GLFW.glfwSetErrorCallback(null).free();
      System.out.println("Safely shut down!");
    }
  }

  public static void update(float delta) {
    if (nextView != null) {
      if (view != null) {
        view.unloadContent();
      }
      view = nextView;
      view.initialize();
      nextView = null;
    }

    if (view == null) {
      return;
    }

    if (!view.isContentLoaded()) {
      view.loadContent();
    } else {
      view.update(delta);
    }
  }

  public static void render() {
    glClearColor(1f, 1, 1f, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glfwPollEvents();

    if (view == null) {
      return;
    }

    if (view.isContentLoaded()) {
      view.render();
    }

    sync(Botanico.FPS_CAP);

    glfwSwapBuffers(WINDOW.getHandle());
  }

  private static void run() {
    RUNNING = true;
    while (RUNNING) {

      long time = System.nanoTime();
      DELTA = (time - LAST_LOOP_TIME) / 1_000_000_000d;
      FPS = 1d / DELTA;
      LAST_LOOP_TIME = time;

      if (WINDOW.shouldClose()) {
        RUNNING = false;
      }

      update((float) DELTA);
      render();
    }
  }

  public static ResourceManager getResourceManager() {
    return RESOURCE_MANAGER;
  }

  public static synchronized GameView getGame() {
    synchronized (view) {
      if (view instanceof GameView) {
        return (GameView) view;
      }
      return null;
    }
  }

  public static void close() {
    RUNNING = false;
  }

  public static RendererGUI getRendererGUI() {
    if (getView() instanceof GameView) {
      return getGame().rendererGUI;
    } else if (getView() instanceof MenuView) {
      return ((MenuView) getView()).getRendererGUI();
    }
    return null;
  }

  private static long variableYieldTime, lastTime;

  /**
   * An accurate sync method that adapts automatically
   * to the system it runs on to provide reliable results.
   *
   * @param fps The desired frame rate, in frames per second
   * @author kappa (On the LWJGL Forums)
   */
  private static void sync(int fps) {
    if (fps <= 0) return;

    long sleepTime = 1000000000 / fps; // nanoseconds to sleep this frame
    // yieldTime + remainder micro & nano seconds if smaller than sleepTime
    long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
    long overSleep = 0; // time the sync goes over by

    try {
      while (true) {
        long t = System.nanoTime() - lastTime;

        if (t < sleepTime - yieldTime) {
          Thread.sleep(1);
        }else if (t < sleepTime) {
          // burn the last few CPU cycles to ensure accuracy
          Thread.yield();
        }else {
          overSleep = t - sleepTime;
          break; // exit while loop
        }
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }finally{
      lastTime = System.nanoTime() - Math.min(overSleep, sleepTime);

      // auto tune the time sync should yield
      if (overSleep > variableYieldTime) {
        // increase by 200 microseconds (1/5 a ms)
        variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
      }
      else if (overSleep < variableYieldTime - 200*1000) {
        // decrease by 2 microseconds
        variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
      }
    }
  }
}
