package me.cmastudios.zerohour;

import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * A game using Slick2d
 */
public class Game extends BasicGame {
	
	enum PlayerType {
		UNDECIDED,
		CYBORG,
		SOLDAT
	}
	
	enum Direction {
		LEFT,
		RIGHT
	}

    /** Screen width */
    private static final int WIDTH = 800;
    /** Screen height */
    private static final int HEIGHT = 600;
        
    private Image selectionScreen;
    private Image backdrop;
    private Image win;
    private Image lose;
    private Image cyborgImage;
    private Image soldatImage;
    private Image bulletImage;
    
    private PlayerType playingAs;
    
    private int cyborgX;
    private int cyborgY;
    
    private int soldatX;
    private int soldatY;
    
    private int bulletX;
    private int bulletY;
    
    private Direction bulletDir;
    
    private Random rand;
    
    private int i;

    public Game() {
        super("Future Cyborg Jihad");
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
    	if (playingAs == PlayerType.UNDECIDED) {
    		g.drawImage(selectionScreen, 0, 0);
    		return;
    	}
    	if (i == 1) {
    		g.drawImage(lose, 0, 0);
    		return;
    	} else if (i == 2) {
    		g.drawImage(win, 0, 0);
    		return;
    	}
    	g.drawImage(backdrop, 0, 0);
    	g.drawImage(cyborgImage, cyborgX, cyborgY);
    	g.drawImage(soldatImage, soldatX, soldatY);
    	if (bulletX != 0 || bulletY != 0) {
    		g.drawImage(bulletImage, bulletX, bulletY);
    	}
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        selectionScreen = new Image(Game.class.getClassLoader().getResourceAsStream("choose.png"), "choose", false);
        backdrop = new Image(Game.class.getClassLoader().getResourceAsStream("earth.png"), "earth", false);
        win = new Image(Game.class.getClassLoader().getResourceAsStream("win.png"), "win", false);
        lose = new Image(Game.class.getClassLoader().getResourceAsStream("dead.png"), "dead", false);
        cyborgImage = new Image(Game.class.getClassLoader().getResourceAsStream("cyborg.png"), "cyborg", false);
        soldatImage = new Image(Game.class.getClassLoader().getResourceAsStream("soldat.png"), "soldat", false);
        bulletImage = new Image(Game.class.getClassLoader().getResourceAsStream("lazer.png"), "lazer", false);
        
        playingAs = PlayerType.UNDECIDED;
        cyborgX = 79;
        cyborgY = 114;
        
        soldatX = 623;
        soldatY = 214;
        
        bulletX = 0;
        bulletY = 0;
        
        rand = new Random();
        
        i = 0;
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
    	if (i != 0)
    		return;
    	if (playingAs == PlayerType.UNDECIDED) {
    		if (container.getInput().isMousePressed(0)) {
    			if (container.getInput().getMouseX() > 66 && container.getInput().getMouseY() > 166
    					&& container.getInput().getMouseX() < 300 && container.getInput().getMouseY() < 400) {
    				playingAs = PlayerType.CYBORG;
    			} else if (container.getInput().getMouseX() > 435 && container.getInput().getMouseY() > 159
    					&& container.getInput().getMouseX() < 672 && container.getInput().getMouseY() < 408) {
    				playingAs = PlayerType.SOLDAT;
    			}    			
    		}
    		return;
    	}
    	if (container.getInput().isKeyDown(Input.KEY_W)) {
    		if (playingAs == PlayerType.CYBORG) {
    			cyborgY -= 5;
    		} else {
    			soldatY -= 5;
    		}
    	}
    	if (container.getInput().isKeyDown(Input.KEY_S)) {
    		if (playingAs == PlayerType.CYBORG) {
    			cyborgY += 5;
    		} else {
    			soldatY += 5;
    		}
    	}
    	if (playingAs == PlayerType.CYBORG) {
    		if (soldatY - cyborgY > 0) {
    			soldatY -= 2;
    		} else {
    			soldatY += 2;
    		}
    	} else {
    		if (cyborgY - soldatY > 0) {
    			cyborgY -= 2;
    		} else {
    			cyborgY += 2;
    		}
    	}
    	if (container.getInput().isKeyDown(Input.KEY_SPACE)) {
    		if (bulletX == 0 && bulletY == 0) {
    			if (playingAs == PlayerType.CYBORG) {
    				bulletX = cyborgX + 70;
    				bulletY = cyborgY;
    				bulletDir = Direction.RIGHT;
    			} else {
    				bulletX = soldatX - 70;
    				bulletY = soldatY;
    				bulletDir = Direction.LEFT;
    			}
    		}
    	}
    	if (rand.nextInt(100) == 37) {
    		if (bulletX == 0 && bulletY == 0) {
    			if (playingAs == PlayerType.SOLDAT) {
    				bulletX = cyborgX + 70;
    				bulletY = cyborgY;
    				bulletDir = Direction.RIGHT;
    			} else {
    				bulletX = soldatX - 70;
    				bulletY = soldatY;
    				bulletDir = Direction.LEFT;
    			}
    		}
    	}
    	if (bulletX != 0 || bulletY != 0) {
    		if (bulletDir == Direction.RIGHT) {
    			bulletX += 60;
    		} else {
    			bulletX -= 60;
    		}
    	}
    	if (bulletX < 0 || bulletX > 800 || bulletY < 0 || bulletY > 600) {
    		bulletX = 0;
    		bulletY = 0;
    	}
		if (Math.abs(bulletY - soldatY) < 32 && bulletX - soldatX > 0) {
			if (playingAs == PlayerType.SOLDAT) {
				i = 1;
			} else {
				i = 2;
			}
		}
		if (Math.abs(bulletY - cyborgY) < 32 && bulletX - cyborgX < 0) {
			if (playingAs == PlayerType.CYBORG) {
				i = 1;
			} else {
				i = 2;
			}
		}
    }
    
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Game());
        app.setDisplayMode(WIDTH, HEIGHT, false);
        app.setForceExit(false);
        app.setTargetFrameRate(30);
        app.start();
    }

}
