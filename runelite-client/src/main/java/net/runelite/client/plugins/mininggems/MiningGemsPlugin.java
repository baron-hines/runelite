package net.runelite.client.plugins.mininggems;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.ScreenAdjustedNature;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.stream.IntStream;

import static net.runelite.api.Perspective.localToCanvas;
import static net.runelite.api.coords.LocalPoint.fromWorld;

@Slf4j
@PluginDescriptor(
        name = "Mining Gems"
)
public class MiningGemsPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private MiningGemsConfig config;

    @Inject
    private OverlayManager overlayManager;


    @Inject
    private MiningGemsOverlay miningGemsOverlay;

    private Integer currentStep = 0;

    private final WorldPoint[] miningRoute = new WorldPoint[]{
            new WorldPoint(2843, 9392, 0),
            new WorldPoint(2844, 9391, 0),
            new WorldPoint(2845, 9391, 0),
            new WorldPoint(2846, 9390, 0),
            new WorldPoint(2847, 9390, 0),
            new WorldPoint(2848, 9390, 0),
            new WorldPoint(2848, 9386, 0),
            new WorldPoint(2849, 9387, 0),
            new WorldPoint(2853, 9389, 0),
            new WorldPoint(2852, 9390, 0),
            new WorldPoint(2854, 9388, 0),
            new WorldPoint(2857, 9386, 0),

// These are two gem spots that we /should/ mine, but it causes issues for now.
//            new WorldPoint(2857, 9385, 0),
//            new WorldPoint(2857, 9382, 0),

            new WorldPoint(2856, 9382, 0),
            new WorldPoint(2855, 9382, 0),
            new WorldPoint(2853, 9380, 0),
            new WorldPoint(2851, 9377, 0),
            new WorldPoint(2850, 9378, 0),
            new WorldPoint(2848, 9379, 0),
            new WorldPoint(2847, 9379, 0),
            new WorldPoint(2847, 9381, 0),

            // Bank Deposit Box
            new WorldPoint(2842, 9382, 0),
    };

    @Override
    protected void startUp() throws Exception
    {
        currentStep = 0;
    }

    @Override
    protected void shutDown() throws Exception
    {
        currentStep = 0;
    }

    @Provides
    MiningGemsConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(MiningGemsConfig.class);
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) throws InterruptedException {
        final GameObject object = event.getGameObject();

        if (object.getId() != ObjectID.ROCKS1 && object.getId() != ObjectID.GEMROCK1) {
            return;
        }

        WorldPoint currentTileWorldPoint = event.getTile().getWorldLocation();

        int i = IntStream.range(0, miningRoute.length)
                .filter(worldPoint-> miningRoute[worldPoint].getX() == currentTileWorldPoint.getX() && miningRoute[worldPoint].getY() == currentTileWorldPoint.getY())
                .findFirst().orElse(-1);

        if (i != -1 && currentStep == i) {
            WorldPoint nextLocation = miningRoute[i+1];

            LocalPoint localPoint = fromWorld(client, nextLocation);
            Point point = localToCanvas(client, localPoint, 0);

            Dimension d = client.getRealDimensions();

            MouseMotionFactory factory = new MouseMotionFactory();
            java.awt.Point offset = new java.awt.Point(0, 22);
            factory.setNature(new ScreenAdjustedNature(d, offset));

            currentStep = i + 1;

            Thread clickThread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(50);

                        factory.move(point.getX(), point.getY());

                        try {
                            Robot r = new Robot();
                            r.mousePress(InputEvent.BUTTON1_MASK);
                            r.mouseRelease(InputEvent.BUTTON1_MASK);

                            if (currentStep == miningRoute.length - 1) {
                                Thread.sleep(4000);

                                factory.move(771, 600);
                                r.mousePress(InputEvent.BUTTON1_MASK);
                                r.mouseRelease(InputEvent.BUTTON1_MASK);

                                Thread.sleep(1000);

                                r.keyPress(KeyEvent.VK_ESCAPE);
                                r.keyRelease(KeyEvent.VK_ESCAPE);

                                currentStep = 0;

                                LocalPoint localPoint = fromWorld(client, miningRoute[currentStep]);
                                Point point = localToCanvas(client, localPoint, 0);

                                Thread.sleep(50);
                                factory.move(point.getX(), point.getY());
                                r.mousePress(InputEvent.BUTTON1_MASK);
                                r.mouseRelease(InputEvent.BUTTON1_MASK);

                            }

                        } catch (AWTException e) {
                        }
                    } catch(InterruptedException v) {
                        System.out.println(v);
                    }
                }
            };

            clickThread.start();

        } else {
            if( i != -1) {

            } else if (currentStep == i){

            } else {
            }

        }
    }
}

