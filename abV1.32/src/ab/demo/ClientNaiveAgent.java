/*****************************************************************************
 ** ANGRYBIRDS AI AGENT FRAMEWORK
 ** Copyright (c) 2014, XiaoYu (Gary) Ge, Stephen Gould, Jochen Renz
 **  Sahan Abeyasinghe,Jim Keys,  Andrew Wang, Peng Zhang
 ** All rights reserved.
**This work is licensed under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
**To view a copy of this license, visit http://www.gnu.org/licenses/
 *****************************************************************************/
package ab.demo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;


import ab.demo.other.ClientActionRobot;
import ab.demo.other.ClientActionRobotJava;
import ab.planner.TrajectoryPlanner;
import ab.vision.ABObject;
import ab.vision.ABType;
import ab.vision.GameStateExtractor.GameState;
import ab.vision.Vision;
import javafx.collections.FXCollections;

import javax.imageio.ImageIO;
//Naive agent (server/client version)

public class ClientNaiveAgent implements Runnable {


	//Wrapper of the communicating messages
	private ClientActionRobotJava ar;
	public byte currentLevel = -1;
	public int failedCounter = 0;
	public int[] solved;
	TrajectoryPlanner tp; 
	private int id = 28888;
	private boolean firstShot;
	private boolean dominoStructureDestroyed;
	private Point prevTarget;
	private Random randomGenerator;
	boolean alreadyDoneInThisLevel = false;
	private ArrayList<ABObject> dominoBlockList = null;
	int continuousLevelCounter = 1;

	/**
	 * Constructor using the default IP
	 * */
	public ClientNaiveAgent() {
		// the default ip is the localhost
		ar = new ClientActionRobotJava("127.0.0.1");
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;
		dominoStructureDestroyed = false;


	}
	/**
	 * Constructor with a specified IP
	 * */
	public ClientNaiveAgent(String ip) {
		ar = new ClientActionRobotJava(ip);
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;
		dominoStructureDestroyed = false;

	}
	public ClientNaiveAgent(String ip, int id)
	{
		ar = new ClientActionRobotJava(ip);
		tp = new TrajectoryPlanner();
		randomGenerator = new Random();
		prevTarget = null;
		firstShot = true;
		dominoStructureDestroyed = false;
		this.id = id;
	}
	public int getNextLevel()
	{
		int level = 0;
		boolean unsolved = false;
		//all the level have been solved, then get the first unsolved level
		for (int i = 0; i < solved.length; i++)
		{
			if(solved[i] == 0 )
			{
					unsolved = true;
					level = i + 1;
					if(level <= currentLevel && currentLevel < solved.length)
						continue;
					else
						return level;
			}
		}
		if(unsolved)
			return level;
	    level = (currentLevel + 1)%solved.length;
		if(level == 0)
			level = solved.length;
		return level; 
	}
    /* 
     * Run the Client (Naive Agent)
     */
	private void checkMyScore()
	{
		
		int[] scores = ar.checkMyScore();
		System.out.println(" My score: ");
		int level = 1;
		for(int i: scores)
		{
			System.out.println(" level " + level + "  " + i);
			if (i > 0)
				solved[level - 1] = 1;
			level ++;
		}
	}
	public void run() {
		byte[] info = ar.configure(ClientActionRobot.intToByteArray(id));
		solved = new int[info[2]];
		fetchNewLevels(1); //get new levels in the beginning
		ar.loadLevel((byte) 15); // load random level in the beginning in order to override level cache

		//load the initial level (default 1)
		//Check my score
		//checkMyScore();

		currentLevel = (byte) getNextLevel();
		ar.loadLevel(currentLevel);
		//ar.loadLevel((byte)9);
		GameState state;
		int levelCounter = 1;

		/*while (true) {
			System.out.println("saving screenshot for level " + currentLevel);
			// capture Image
			ar.fullyZoomOut();
			BufferedImage screenshot = ar.doScreenShot();
			//BufferedImage screenShotMintLevel = screenshot;

			// process image
			//Vision vision = new Vision(screenshot);
			saveScreenshot(currentLevel, screenshot, "noDominoStructure");
			currentLevel = (byte) getNextLevel();
			ar.loadLevel(currentLevel);
		}*/

		/*
		try {
			FileUtils.cleanDirectory(new File("screenshots/dominoStructure"));
			FileUtils.cleanDirectory(new File("screenshots/noDominoStructure"));
			FileUtils.cleanDirectory(new File("screenshots/uncertain"));
		} catch (IOException e) {
			e.printStackTrace();
		} */
		try {
			Files.deleteIfExists(Paths.get("screenshots/coordinates.txt"));
			Files.createFile(Paths.get("screenshots/coordinates.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int levelFolderCounter = 2;
		while (true) {
			if (continuousLevelCounter > 1998) {
				break;
			}
			System.out.println("while true: running level " + currentLevel);

			dominoStructureDestroyed = false;
			state = solve(currentLevel);
			//If the level is solved , go to the next level
			if (state == GameState.WON) {
				alreadyDoneInThisLevel = false;
				dominoStructureDestroyed = false;
				///System.out.println(" loading the level " + (currentLevel + 1) );
				//checkMyScore();
				currentLevel = (byte)getNextLevel();
				continuousLevelCounter++;
				ar.loadLevel(currentLevel);
				//display the global best scores
				/*
				int[] scores = ar.checkScore();

				System.out.println("Global best score: ");
				for (int i = 0; i < scores.length ; i ++)
				{
					System.out.print( " level " + (i+1) + ": " + scores[i]);
				}
				System.out.println();
				*/

				// make a new trajectory planner whenever a new level is entered
				tp = new TrajectoryPlanner();


				// first shot on this level, try high shot first
				firstShot = true;

			} else
				//If lost, then restart the level
				if (state == GameState.LOST) {
					dominoStructureDestroyed = false;
					alreadyDoneInThisLevel = false;

					failedCounter++;
				if(failedCounter > 3)
				{
					failedCounter = 0;
					currentLevel = (byte)getNextLevel();
					continuousLevelCounter++;
					ar.loadLevel(currentLevel);

					//ar.loadLevel((byte)9);
				}
				else
				{
					alreadyDoneInThisLevel = false;
					dominoStructureDestroyed = false;
					System.out.println("restart");
					ar.restartLevel();
				}

			} else
				if (state == GameState.LEVEL_SELECTION) {
					alreadyDoneInThisLevel = false;
					dominoStructureDestroyed = false;

					System.out.println("unexpected level selection page, go to the last current level : "
								+ currentLevel);
				ar.loadLevel(currentLevel);
			} else if (state == GameState.MAIN_MENU) {
					alreadyDoneInThisLevel = false;
					dominoStructureDestroyed = false;

					System.out
						.println("unexpected main menu page, reload the level : "
								+ currentLevel);
				ar.loadLevel(currentLevel);
			} else if (state == GameState.EPISODE_MENU) {
					alreadyDoneInThisLevel = false;
					dominoStructureDestroyed = false;

					System.out.println("unexpected episode menu page, reload the level: "
								+ currentLevel);
				ar.loadLevel(currentLevel);
			}
			// if currentlevel > 19, copy new levels to folder and set currentlevel to 1 again
			if (currentLevel == 20){
				fetchNewLevels(levelFolderCounter);
			}
			if (currentLevel == 21){
				currentLevel = -1;
				levelFolderCounter++;
				System.out.println("setting currentLevel to -1");
			}

		}


	}

	private void fetchNewLevels(int levelFolderCounter) {
		for (int i = 0; i < 21; i++){
            Path createdLevelPath = Paths.get("/Users/felix/Documents/git/aiBirds/levelGenerator/generatedLevels/dominoLevels/levels" + levelFolderCounter + "/Level1-" + (i+1) + ".json");
            Path gameLevelPath = Paths.get("/Users/felix/Documents/git/BamBird_2017/game/slingshot/cors/fowl/json/Level1-" + (i+1) + ".json");
            try {
                Files.copy(createdLevelPath, gameLevelPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("something went wrong.\ncreatedLevelPath: " + createdLevelPath + "\ngameLevelPath: " + gameLevelPath);
                e.printStackTrace();
            }
        }
		System.out.println("levels overwritten for the " + levelFolderCounter +"st time.");
	}


	/**
	   * Solve a particular level by shooting birds directly to pigs
	   * @return GameState: the game state after shots.
     */
	public GameState solve(int levelCounter) {
		System.out.println("solve()");
		// capture Image
		BufferedImage screenshot = ar.doScreenShot();
		BufferedImage screenShotMintLevel = ar.doScreenShot();

		// process image
		Vision vision = new Vision(screenshot);

		Rectangle sling = vision.findSlingshotMBR();

		//If the level is loaded (in PLAYING　state)but no slingshot detected, then the agent will request to fully zoom out.
		while (sling == null && ar.checkState() == GameState.PLAYING) {
			System.out.println("no slingshot detected. Please remove pop up or zoom out");

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			ar.fullyZoomOut();
			screenshot = ar.doScreenShot();
			vision = new Vision(screenshot);
			sling = vision.findSlingshotMBR();
		}


		 // get all the pigs
 		List<ABObject> pigs = vision.findPigsMBR();
		// get all the blocks
		ArrayList<ABObject> allBlocksList = new ArrayList<>(vision.findBlocksMBR());
		//ArrayList<ABObject> allBlocksListRealShape = new ArrayList<>(vision.findBlocksRealShape());

		System.out.println("slingshot: " + vision.findSlingshotMBR());
		System.out.println("recognized blocks by MBR: " + allBlocksList.size());
		//System.out.println("recognized blocks by RS: " + allBlocksListRealShape.size());

		/*if (!(allBlocksList.size() == allBlocksListRealShape.size())) {
			System.out.println("Care! Discrepancy between vision modules! Not all blocks are being printed!");
		}*/

		sortBlocksByXValue(allBlocksList);
		//sortBlocksByXValue(allBlocksListRealShape);
		for (ABObject block : allBlocksList){
			System.out.println("MBR: " + block + " " + block.getType() + " centerY(): " + block.getCenterY());
		}
		/*for (ABObject block : allBlocksListRealShape){
			System.out.println("RS:  " + block + " " + block.getType());
			System.out.println("blockangle RS: " + block.angle);
		}*/

		GameState state = ar.checkState();
		// if there is a sling, then play, otherwise skip.
		if (sling != null) {
			System.out.println("domino structure in this level is destroyed: " + dominoStructureDestroyed);
			if (!alreadyDoneInThisLevel) {
				dominoBlockList = getDominoBlocks(allBlocksList);
				if (dominoBlockList.isEmpty()){
					System.out.println("1");
					saveScreenshot(screenShotMintLevel, "noDominoStructure");
					return GameState.WON; // exiting level, no pig destruction necessary any more. Can be removed in order to get back to regular behaviour.
				}
				alreadyDoneInThisLevel = true;
			}

			if (dominoBlockList.isEmpty()) {
				dominoStructureDestroyed = true;
			}

			if (!dominoStructureDestroyed) {
				// fange hier an, auf Dominoblocks zu schießen
				Point releasePoint = null;
				//check if allBlocksList contains three times the same block
				sortBlocksByXValue(dominoBlockList);
				ABObject dominoBlock = dominoBlockList.get(0);

				// check if domino block is made from ice - then it's no domino structure (no need any more, is already in getDominoBlocks())
				/*
				if (dominoBlock.getType() == ABType.Ice) {
					System.out.println("2 (ice)");
					saveScreenshot(screenShotMintLevel, "noDominoStructure");
					return GameState.WON; // exiting level, no pig destruction necessary any more. Can be removed in order to get back to regular behaviour.
				}
				*/

				System.out.println("first dominoblock: " + dominoBlock);

				Point _tpt = new Point((int) dominoBlock.getX(), ((int) dominoBlock.getY() + 10)); // add one fourth to y-value of center so it shoots on top half of block

				prevTarget = new Point(_tpt.x, (_tpt.y + 10)); // add one fourth to y-value of center so it shoots on top half of block

				// estimate the trajectory
				ArrayList<Point> pts = tp.estimateLaunchPoint(sling, _tpt);
				System.out.println("pts size(): " + pts.size());
				int ptsCounter = 0;
				while (pts.isEmpty()){
					pts = tp.estimateLaunchPoint(sling, _tpt);
					ptsCounter++;
					if (ptsCounter > 10){
						pts = tp.estimateLaunchPoint(sling, pigs.get(0).getCenter()); // if no launchpoint for domino is found, shoot on pig instead
					}
				}

				/*if 0e() == 1)
					releasePoint = pts.get(0);
				else if (pts.size() == 2) {
					// System.out.println("first shot " + firstShot);
					// randomly choose between the trajectories, with a 1 in
					// 6 chance of choosing the high one
					if (randomGenerator.nextInt(6) == 0)
						releasePoint = pts.get(1);
					else
						releasePoint = pts.get(0);
				}*/
				releasePoint = pts.get(0);

				Point refPoint = tp.getReferencePoint(sling);
				System.out.println("refpoint: " + refPoint);

				// Get the release point from the trajectory prediction module
				int tapTime = 0;
				if (releasePoint != null) {
					double releaseAngle = tp.getReleaseAngle(sling,
							releasePoint);
					System.out.println("Release Point: " + releasePoint);
					System.out.println("Release Angle: "
							+ Math.toDegrees(releaseAngle));
					int tapInterval = 0;
					tapTime = tp.getTapTime(sling, releasePoint, _tpt, tapInterval);

				} else {
					System.err.println("No Release Point Found");
					return ar.checkState();
				}


				// check whether the slingshot is changed. the change of the slingshot indicates a change in the scale.
				ar.fullyZoomOut();
				screenshot = ar.doScreenShot();
				vision = new Vision(screenshot);
				Rectangle _sling = vision.findSlingshotMBR();

				// shoot
				if (_sling != null) {
					double scale_diff = Math.pow((sling.width - _sling.width), 2) + Math.pow((sling.height - _sling.height), 2);
					if (scale_diff < 25) {
						int dx = (int) releasePoint.getX() - refPoint.x;
						int dy = (int) releasePoint.getY() - refPoint.y;
						if (dx < 0) {
							long timer = System.currentTimeMillis();
							ar.shoot(refPoint.x, refPoint.y, dx, dy, 0, tapTime, false);
							System.out.println("It takes " + (System.currentTimeMillis() - timer) + " ms to take a shot");

							// check if domino structure is destroyed
							ArrayList<ABObject> oldDominoBlockList = new ArrayList<>(dominoBlockList);
							dominoBlockList = removeDestroyedBlocksFromDominoList(oldDominoBlockList);
							System.out.println("new dominoBlocklist after shot: " + dominoBlockList);

							if (dominoBlockList.isEmpty()) {
								System.out.println("3 (Domino structure destroyed!)");
								dominoStructureDestroyed = true;
								saveScreenshot(screenShotMintLevel, "dominoStructure");
								BufferedImage screenshotWithX = drawXonScreenshot(screenShotMintLevel, oldDominoBlockList.get(0).getCenterX(), (oldDominoBlockList.get(0).getY() + 10), levelCounter);
								saveScreenshot(screenshotWithX, "screenshotsWithX");
								writeCoordinatesToFile(("level" + continuousLevelCounter), oldDominoBlockList.get(0).getCenterX(), (oldDominoBlockList.get(0).getY() + 10));
								return GameState.WON; // exiting level, no pig destruction necessary any more. Can be removed in order to get back to regular behaviour.
							} else {
								System.out.println("Domino structure NOT destroyed!");
								saveScreenshot(screenShotMintLevel, "noDominoStructure");
								dominoStructureDestroyed = false;
								return GameState.WON; // exiting level, no pig destruction necessary any more. Can be removed in order to get back to regular behaviour.
							}

							/*
							state = ar.checkState();
							if (state == GameState.PLAYING) {
								screenshot = ar.doScreenShot();
								vision = new Vision(screenshot);
								List<Point> traj = vision.findTrajPoints();
								tp.adjustTrajectory(traj, sling, releasePoint);
								firstShot = false; // don't mark firstShot = false, so that the next shot will still be a high shot on the only pig in the game
							}
							*/
						}
					} else
						System.out.println("Scale is changed, can not execute the shot, will re-segement the image");
				} else
					System.out.println("no sling detected, can not execute the shot, will re-segement the image");
			}

			// in case domino structure also destroys pigs, we need to do a new screenshot
			ar.fullyZoomOut();
			BufferedImage pigsScreenshot = ar.doScreenShot();
			Vision pigsVision = new Vision(pigsScreenshot);
			pigs = pigsVision.findPigsMBR();

			//If there are pigs, we pick up a pig randomly and shoot it.
			if (!pigs.isEmpty() && dominoStructureDestroyed) {
				System.out.println("shooting on a pig...");
				Point releasePoint = null;
				// random pick up a pig
				ABObject pig = pigs.get(randomGenerator.nextInt(pigs.size()));

				Point _tpt = pig.getCenter();

				// if the target is very close to before, randomly choose a
				// point near it
				/*if (prevTarget != null && distance(prevTarget, _tpt) < 10) {
					double _angle = randomGenerator.nextDouble() * Math.PI * 2;
					_tpt.x = _tpt.x + (int) (Math.cos(_angle) * 10);
					_tpt.y = _tpt.y + (int) (Math.sin(_angle) * 10);
					System.out.println("Randomly changing to " + _tpt);
				}*/

				prevTarget = new Point(_tpt.x, _tpt.y);

				// estimate the trajectory
				ArrayList<Point> pts = tp.estimateLaunchPoint(sling, _tpt);

				// do a high shot when entering a level to find an accurate velocity
				if (firstShot && pts.size() > 1) {
					releasePoint = pts.get(1);
				} else if (pts.size() == 1) {
					releasePoint = pts.get(0);
				}
				else if (pts.size() == 2) {
					// System.out.println("first shot " + firstShot);
					// randomly choose between the trajectories, with a 1 in
					// 6 chance of choosing the high one
					if (randomGenerator.nextInt(6) == 0)
						releasePoint = pts.get(1);
					else
						releasePoint = pts.get(0);
				}
				Point refPoint = tp.getReferencePoint(sling);

				// Get the release point from the trajectory prediction module
				int tapTime = 0;
				if (releasePoint != null) {
					double releaseAngle = tp.getReleaseAngle(sling,
							releasePoint);
					System.out.println("Release Point: " + releasePoint);
					System.out.println("Release Angle: "
							+ Math.toDegrees(releaseAngle));
					int tapInterval = 0;
					switch (ar.getBirdTypeOnSling()) {

						case RedBird:
							tapInterval = 0;
							break;               // start of trajectory
						case YellowBird:
							tapInterval = 65 + randomGenerator.nextInt(25);
							break; // 65-90% of the way
						case WhiteBird:
							tapInterval = 50 + randomGenerator.nextInt(20);
							break; // 50-70% of the way
						case BlackBird:
							tapInterval = 0;
							break; // 70-90% of the way
						case BlueBird:
							tapInterval = 65 + randomGenerator.nextInt(20);
							break; // 65-85% of the way
						default:
							tapInterval = 60;
					}

					tapTime = tp.getTapTime(sling, releasePoint, _tpt, tapInterval);

				} else {
					System.err.println("No Release Point Found");
					return ar.checkState();
				}


				// check whether the slingshot is changed. the change of the slingshot indicates a change in the scale.
				ar.fullyZoomOut();
				screenshot = ar.doScreenShot();
				vision = new Vision(screenshot);
				Rectangle _sling = vision.findSlingshotMBR();
				if (_sling != null) {
					double scale_diff = Math.pow((sling.width - _sling.width), 2) + Math.pow((sling.height - _sling.height), 2);
					if (scale_diff < 25) {
						int dx = (int) releasePoint.getX() - refPoint.x;
						int dy = (int) releasePoint.getY() - refPoint.y;
						if (dx < 0) {
							long timer = System.currentTimeMillis();
							ar.shoot(refPoint.x, refPoint.y, dx, dy, 0, tapTime, false);
							System.out.println("It takes " + (System.currentTimeMillis() - timer) + " ms to take a shot");
							state = ar.checkState();
							if (state == GameState.PLAYING) {
								screenshot = ar.doScreenShot();
								vision = new Vision(screenshot);
								List<Point> traj = vision.findTrajPoints();
								tp.adjustTrajectory(traj, sling, releasePoint);
								firstShot = false;
							}
						}
					} else {
						System.out.println("Scale is changed, can not execute the shot, will re-segement the image");
					}
				} else {
					System.out.println("no sling detected, can not execute the shot, will re-segement the image");
				}
			} else if (pigs.isEmpty()) {
				System.out.println("Pigs empty! ERROR!");
			}
		}
		//return state;
		return GameState.WON;
	}

	private ArrayList<ABObject> getDominoBlocks(List<ABObject> blockList) {
		sortBlocksByXValue(new ArrayList<>(blockList));
		ArrayList<ABObject> dominoBlockList = new ArrayList<>();

		int counter = 0;
		ArrayList<ABObject> tempDominoBlockList = new ArrayList<>();

		for (int j = 0; j < blockList.size(); j++){
			ABObject innerLoopBlock = blockList.get(j);
			//boolean yValueHighEnough = equalsWithinMargin(innerLoopBlock.getY(), outerLoopBlock.getY(), 1);
			boolean withinDistanceToNeighboringBlocks = false;
			boolean distanceToNeighbouringBlocksHighEnough = false;
			boolean blockHeightAtLeastDoubleAsWidth = false;
			boolean iceBlock = false;
			boolean woodBlockAndTooNear = false;

			// if block is made from wood and too near, wood will be destroyed instead of falling over
			if (innerLoopBlock.getType() == ABType.Wood && innerLoopBlock.getX() < 340){
				woodBlockAndTooNear = true;
			}

			if (innerLoopBlock.getType() == ABType.Ice){
				iceBlock = true;
			}

			//not really necessary, since domino structure only uses vertical high blocks
			if (innerLoopBlock.getHeight()*2 > innerLoopBlock.getWidth()){
				blockHeightAtLeastDoubleAsWidth = true;
			}

			// check for next block (in case we're looking at the first block)
			if (j == 0) { // first block
				ABObject nextBlock = blockList.get(j + 1);
				double distance = Math.abs(innerLoopBlock.getX() - nextBlock.getX());

				// check for difference in height not bigger than 2/3 the distance
				if (distance < innerLoopBlock.getHeight()) {
					withinDistanceToNeighboringBlocks = true;
				}
				// check for distance to be bigger than a small amount. Else blocks won't fall.
				if (distance > 2) {
					distanceToNeighbouringBlocksHighEnough = true;
				}

				// check for next and prev block
			} else if (j < (blockList.size() - 1)) { // every other block except the last one
				ABObject nextBlock = blockList.get(j + 1);
				ABObject prevBlock = blockList.get(j - 1);
				double distanceToNextBlock = Math.abs(innerLoopBlock.getX() - nextBlock.getX());
				double distanceToPrevBlock = Math.abs(innerLoopBlock.getX() - prevBlock.getX());



				// check for distance to be bigger than a small amount. Else blocks won't fall.
				if (distanceToNextBlock > 2 && distanceToPrevBlock > 2) {
					distanceToNeighbouringBlocksHighEnough = true;
				}


				if (distanceToNextBlock < innerLoopBlock.getHeight() ||
						(distanceToPrevBlock < innerLoopBlock.getHeight()))	{
					withinDistanceToNeighboringBlocks = true;
				}


				// check for prev block (in case we're looking at the last block)
			} else if (j == (blockList.size() - 1)) { // last block
				ABObject prevBlock = blockList.get(j - 1);
				double distanceToPrevBlock = Math.abs(innerLoopBlock.getX() - prevBlock.getX());

				if (distanceToPrevBlock > 2) {
					distanceToNeighbouringBlocksHighEnough = true;
				}

				if (distanceToPrevBlock < innerLoopBlock.getHeight()) {
					withinDistanceToNeighboringBlocks = true;
				}
			}


			System.out.println("block: " + innerLoopBlock
					+ "\n withinDistanceToNeighboringBlocks: " + withinDistanceToNeighboringBlocks
					+ "\n distanceToNeighbouringBlocksHighEnough: " + distanceToNeighbouringBlocksHighEnough
					+ "\n blockHeightAtLeastDoubleAsWidth: " + blockHeightAtLeastDoubleAsWidth
					+ "\n innerLoopBlock.getWidth() <= 8: " + (innerLoopBlock.getWidth() <= 8)
					+ "\n innerLoopBlock.getHeight() >= 28: " + (innerLoopBlock.getHeight() >= 28)
					+ "\n !iceBlock: " + !iceBlock
					//+ "\n !woodBlockAndTooNear: " + !woodBlockAndTooNear
			);

			if (withinDistanceToNeighboringBlocks
					&& distanceToNeighbouringBlocksHighEnough
					&& blockHeightAtLeastDoubleAsWidth
					&& !iceBlock
					//&& !woodBlockAndTooNear
					&& innerLoopBlock.getWidth() <= 8
					&& innerLoopBlock.getHeight() >= 28) {
				//System.out.println("adding block: " + innerLoopBlock);
				counter++;
				tempDominoBlockList.add(innerLoopBlock);
				if (counter >= 3) {
					//System.out.println("counter > 3");
					dominoBlockList = tempDominoBlockList;
				}
			} else {
				counter = 0;
			}
		}

		// remove blocks that look like, but aren't part of the domino structure
		ArrayList<ABObject> realDominoBlocks = new ArrayList<>();
		sortBlocksByXValue(dominoBlockList);
		System.out.println("dominoblocks before removal: " + dominoBlockList);

		for (int i = 0; i < dominoBlockList.size(); i++){
			if (dominoBlockList.size() >= 3){
				ABObject blockToCheck = dominoBlockList.get(i);

				//looking at first block
				if (i == 0){
					double distanceNext = Math.abs(blockToCheck.getX() - dominoBlockList.get(i+1).getX());
					if (distanceNext < blockToCheck.getHeight()){
						realDominoBlocks.add(blockToCheck);
						System.out.println("keeping block " + blockToCheck + ", because " + distanceNext + " < " + blockToCheck.getHeight());
					} else {
						System.out.println("removing block " + blockToCheck + ", because " + distanceNext + " >= " + blockToCheck.getHeight());
					}
				// looking at all but first and last block
				} else if (i < dominoBlockList.size()-1){
					double distancePrev = Math.abs(blockToCheck.getX() - dominoBlockList.get(i-1).getX());
					double distanceNext = Math.abs(blockToCheck.getX() - dominoBlockList.get(i+1).getX());

					if (distancePrev < blockToCheck.getHeight() && distanceNext < blockToCheck.getHeight()){ // both distances have to be low
						realDominoBlocks.add(blockToCheck);
						System.out.println("keeping block " + blockToCheck + ", because " + distancePrev + " and " + distanceNext + " < " + blockToCheck.getHeight());
					} else {
						System.out.println("removing block " + blockToCheck + ", because " + distancePrev + " or " + distanceNext + " >= " + blockToCheck.getHeight());
					}
				//looking alt last block
				} else if (i == dominoBlockList.size()-1){
					double distancePrev = Math.abs(blockToCheck.getX() - dominoBlockList.get(i-1).getX());
					if (distancePrev < blockToCheck.getHeight()){
						realDominoBlocks.add(blockToCheck);
						System.out.println("keeping block " + blockToCheck + ", because " + distancePrev + " < " + blockToCheck.getHeight());
					} else {
						System.out.println("removing block " + blockToCheck + ", because " + distancePrev + " >= " + blockToCheck.getHeight());
					}
				}
			}
        }
		System.out.println("dominoblocks after removal: " + realDominoBlocks);
		return realDominoBlocks;
	}

	private void sortBlocksByXValue(ArrayList<ABObject> dominoBlockList) {
		// sort dominoBlockList ascending
		Collections.sort(dominoBlockList, new Comparator<ABObject>() {
            @Override
            public int compare(ABObject o1, ABObject o2) {
                Integer val1 = (int) o1.getX();
                Integer val2 = (int) o2.getX();
                //return val1 > val2 ? 1 : val2 < val1 ? -1 : 1;
				return val1.compareTo(val2);
            }
        });
	}

	private void saveScreenshot(BufferedImage screenshot, String folderName) {
		// screenshot machen und speichern
		String levelcounterString = Integer.toString(continuousLevelCounter);
		String filename = "level" + levelcounterString + ".png";
		File outputfile = new File("screenshots/" + folderName + "/" + filename);
		try {
			ImageIO.write(screenshot, "png", outputfile);
			System.out.println("Screenshot gespeichert als " + filename);
		} catch (IOException e) {
			System.out.println("Screenshot konnte nicht gespeichert werden.");
			e.printStackTrace();
		}
	}

	private void writeCoordinatesToFile(String levelName, double x, double y) {
		try {
			Files.write(Paths.get("screenshots/coordinates.txt"), ("\n" + levelName + ";" + x + ";" + y + ";").getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double distance(Point p1, Point p2) {
		return Math.sqrt((double) ((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)* (p1.y - p2.y)));
	}

	public static void main(String args[]) {

		ClientNaiveAgent na;
		if(args.length > 0)
			na = new ClientNaiveAgent(args[0]);
		else
			na = new ClientNaiveAgent();
		na.run();
		
	}

	private boolean equalsWithinMargin (double firstVal, double secondVal, double margin){
		return (Math.abs(firstVal - secondVal) <= margin);
	}

	private ArrayList<ABObject> removeDestroyedBlocksFromDominoList(ArrayList<ABObject> originalDominoBlockList){
		BufferedImage newScreenshot = ar.doScreenShot();
		Vision vision = new Vision(newScreenshot);
		ArrayList<ABObject> dominoListAfter = new ArrayList<>();
		ArrayList<ABObject> allBlocksAfter = new ArrayList<>(vision.findBlocksMBR());
		// ab hier
		System.out.println("dominoblocks before: " + originalDominoBlockList);
		allBlocksAfter.removeIf(block -> block.getX() < originalDominoBlockList.get(0).getX());
		allBlocksAfter.removeIf(block -> block.getX() > originalDominoBlockList.get(originalDominoBlockList.size()-1).getX());
		allBlocksAfter.removeIf(b -> b.getHeight() < (originalDominoBlockList.get(0).getHeight() /10*6));
		System.out.println("originalDominoBlockList.get(0).getHeight() / 2): " + originalDominoBlockList.get(0).getHeight() / 2);
		System.out.println("dominoblocks after: " + allBlocksAfter);

		// bis hier
		/*
		System.out.println("originalDominoBlockList.size(): " + originalDominoBlockList.size());
		System.out.println("allBlocksAfter.size(): " + allBlocksAfter.size());
		for (int i = 0; i < originalDominoBlockList.size(); i++){
			System.out.println("looking at Dominoblock " + originalDominoBlockList.get(i) + "\nXValue of dominoBlock is " + originalDominoBlockList.get(i).getX());
			for (ABObject block : allBlocksAfter) {
				//System.out.println("xValue of block to compare is " + block.getX());
				//System.out.println("originalDominoBlockList.get(i).getX(): " + originalDominoBlockList.get(i).getX());
				//System.out.println("block.getX(): " + block.getX());
				if (equalsWithinMargin(originalDominoBlockList.get(i).getX(), block.getX(), 1) && equalsWithinMargin(block.getHeight(), originalDominoBlockList.get(i).getHeight(), 8)) {
					System.out.println("still untouched block: " + originalDominoBlockList.get(i) + "\ndominoBlock.getX(): " + originalDominoBlockList.get(i).getX() + "\n" + "block.getX(): " + block.getX());
					dominoListAfter.add(block);
				}
			}
		}

		return dominoListAfter;
		*/
		return allBlocksAfter;
	}

	public BufferedImage drawXonScreenshot(BufferedImage screenshot, double xVal, double yVal, int levelCounter){
		System.out.println("drawing X on screenshot with x=" + xVal + " and y=" + yVal + " ...");

		// Icon made by twitter from www.flaticon.com
		BufferedImage cross = null;
		try {
			cross = ImageIO.read(new File("close32.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**Create a Graphics  from the background image**/
		Graphics2D g = screenshot.createGraphics();
		/**Set Antialias Rendering**/
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		/**
		 * Draw background image at location (0,0)
		 * You can change the (x,y) value as required
		 */
		g.drawImage(screenshot, 0, 0, null);

		/**
		 * Draw foreground image at location (0,0)
		 * Change (x,y) value as required.
		 */
		int xValue = (int) Math.round(xVal) - 16; // - 16 because we want the center of the cross to be the point
		int yValue = (int) Math.round(yVal) - 16;
		System.out.println("rounded x=" + xValue + "\nrounded y=" + yValue);
		g.drawImage(cross, xValue, yValue, null);
		g.dispose();
		return screenshot;
	}

}
