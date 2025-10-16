package net.runelite.client.plugins.mininggems;

import com.google.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class MiningGemsOverlay extends Overlay
{
    private final Client client;
    private final MiningGemsPlugin plugin;
    private final MiningGemsConfig config;

    @Inject
    MiningGemsOverlay(Client client, MiningGemsPlugin plugin, MiningGemsConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        return null;
    }
}
