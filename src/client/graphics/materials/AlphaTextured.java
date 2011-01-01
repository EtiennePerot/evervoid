package client.graphics.materials;

import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;

public class AlphaTextured extends BaseMaterial
{
	public AlphaTextured(final String texture)
	{
		super("AlphaTextured");
		setBoolean("m_ShowAlpha", true);
		setFloat("m_HueMultiplier", 1.7f);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}

	public void setAlpha(final float alpha)
	{
		setBoolean("m_UseAlphaMultiplier", true);
		setFloat("m_AlphaMultiplier", alpha);
	}

	public void setHue(final ColorRGBA hue)
	{
		if (hue == null)
		{
			setBoolean("m_UseHueColor", false);
		}
		else
		{
			setBoolean("m_UseHueColor", true);
			setColor("m_HueColor", hue);
		}
	}

	public void setHue(final ColorRGBA hue, final float multiplier)
	{
		setHue(hue);
		setHueMultiplier(multiplier);
	}

	public void setHueMultiplier(final float multiplier)
	{
		setFloat("m_HueMultiplier", multiplier);
	}
}
