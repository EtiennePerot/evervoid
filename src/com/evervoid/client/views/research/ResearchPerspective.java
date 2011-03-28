package com.evervoid.client.views.research;

import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.Perspective;

public class ResearchPerspective extends Perspective
{
	public ResearchPerspective(final GameView gameview, final Bounds bounds)
	{
		super(gameview);
		setContent(new ResearchView(bounds));
		// setMini(new MiniResearchView(gameview, galaxy));
	}
}
