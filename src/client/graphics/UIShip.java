package client.graphics;

import com.jme3.math.ColorRGBA;

public class UIShip extends Prop implements Colorable
{
	private final Sprite aColorableSprite;

	public UIShip()
	{
		super();
		addSprite("ships/square/scout_base.png");
		aColorableSprite = addSprite("ships/square/scout_color.png");
	}

	@Override
	public void setHue(final ColorRGBA hue)
	{
		aColorableSprite.setHue(hue);
	}

	@Override
	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		aColorableSprite.setHue(hue, multiplier);
	}
}
