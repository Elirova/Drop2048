package drop2048.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import drop2048.Drop2048;
import drop2048.IActivityRequestHandler;

public class DesktopLauncher implements IActivityRequestHandler {
	private static DesktopLauncher application;
	public static void main (String[] arg) {
		if (application == null) application = new DesktopLauncher();
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop 2048";
		config.width = 400;
		config.height = 600;
		new LwjglApplication(new Drop2048(application), config);
	}

	@Override
	public void showAds(boolean show) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getHeightAd() {
		// TODO Auto-generated method stub
		return 0;
	}
}
