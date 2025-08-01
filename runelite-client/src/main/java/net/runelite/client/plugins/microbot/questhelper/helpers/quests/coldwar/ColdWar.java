package net.runelite.client.plugins.microbot.questhelper.helpers.quests.coldwar;

import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.BasicQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.ObjectCondition;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.LogicType;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Operation;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarbitRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ExperienceReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.QuestPointReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.UnlockReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;

import java.util.*;

public class ColdWar extends BasicQuestHelper
{
	//Items Required
	ItemRequirement oakPlanks, oakPlankHighlight, steelNails, hammer, spade, spadeHighlight, clockworkOrSteelBar,
		clockwork, steelBar, plank, silk, rawCodOrCharos, swampTar, feathers, mahoganyPlank, leather, cowbell,
	    clockworkBookHighlight, clockworkSuit, clockworkSuitHighlight, missionReportHighlight, lumbridgeMissionReport, mahoganyPlankHighlight, leatherHighlight,
		bongos, kgpId;

	//Items Recommended
	ItemRequirement combatGear, teleportArdougne, teleportLumbridge2, teleportHouse;

	Requirement isOnIceberg, birdHideBuilt, tableNearby, isPenguin, isInPenguinPen, isAtZoo, isAtLumbridgeSheepFarm,
		isInAgilityStart, isInAgilityWater, isInAgilityStones, isInAgilityTreadSoftly, isInAgilityCrossIce, isInAgilityDone,
		isInPingPongRoom, isPreWarRoom, isInIcelordPit, isInIcelordRoom, isEmoting, isInPenguinRooms, guardMoved;

	QuestStep talkToLarry, talkToLarryAgain, usePlankOnFirmSnow, useSpadeOnBirdHide, learnPenguinEmotes,
		talkToLarryAfterEmotes, returnToRelleka, talkToLarryInRelleka, enterPoh, makeClockwork, makePenguin,
		bringSuitToLarry, talkToLarryOnIcebergWithSuit, tuxedoTime, enterPenguinPen, talkToZooPenguin, emoteAtPenguin,
		exitSuit, talkToLarryMissionReport, readMissionReport, tuxedoTimeLumbridge, talkToThing, emoteAtPenguinInLumbridge,
		returnToZooPenguin, returnToThing, fredTheFarmer, stealCowbell, askThingAboutOutpost, tellLarryAboutOutpost, kgpAgent,
		emoteAtPenguinOutpost, noodle1, noodle2, kgpAgent2, enterAvalanche, kgpAgentInAvalanche, enterAgilityCourse, agilityCourse,
		agilityEnterWater, agilityExitWater, agilityJumpStones, agilityTreadSoftly, agilityCrossIce, agilityDone,
		tellLarryAboutArmy, kgpBeforePingPong, pingPong1, removePenguinSuitForBongos, makeBongos, pingPong2,
		pingPong3, openControlDoor, enterWarRoom, exitIcelordPen, killIcelords, useChasm, tellLarryPlans,
		enterAvalanche2, enterAvalanche3;

	//Zones
	Zone onIceberg, inPenguinPen, inPenguinPen2, atZoo, atLumbridgeSheepFarm, inAgilityStart, inAgilityWater, inAgilityStones,
		inAgilityTreadSoftly, inAgilityCrossIce, inAgilityDone, inPingPongRoom, preWarRoom, inIcelordPit, inIcelordRoom, inPenguinRooms;

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupConditions();
		setupSteps();

		Map<Integer, QuestStep> steps = new HashMap<>();
		steps.put(0, talkToLarry);

		ConditionalStep settingUpTheBirdHide = new ConditionalStep(this, talkToLarryAgain);
		settingUpTheBirdHide.addStep(birdHideBuilt, useSpadeOnBirdHide);
		settingUpTheBirdHide.addStep(isOnIceberg, usePlankOnFirmSnow);
		steps.put(5, settingUpTheBirdHide);

		steps.put(10, learnPenguinEmotes);

		ConditionalStep larryInAfterEmotes = new ConditionalStep(this, talkToLarryInRelleka);
		larryInAfterEmotes.addStep(isOnIceberg, talkToLarryAfterEmotes);
		steps.put(15, larryInAfterEmotes);

		ConditionalStep larryInRelleka = new ConditionalStep(this, talkToLarryInRelleka);
		larryInRelleka.addStep(isOnIceberg, returnToRelleka);
		steps.put(20, larryInRelleka);

		ConditionalStep clockworkPenguin = new ConditionalStep(this, enterPoh);
		clockworkPenguin.addStep(clockworkSuit, bringSuitToLarry);
		clockworkPenguin.addStep(new Conditions(tableNearby, clockwork), makePenguin);
		clockworkPenguin.addStep(new Conditions(tableNearby), makeClockwork);
		steps.put(25, clockworkPenguin);

		steps.put(30, talkToLarryOnIcebergWithSuit);

		ConditionalStep zooPenguinTrust = new ConditionalStep(this, tuxedoTime);
		zooPenguinTrust.addStep(isEmoting, emoteAtPenguin);
		zooPenguinTrust.addStep(isInPenguinPen, talkToZooPenguin);
		zooPenguinTrust.addStep(isPenguin, enterPenguinPen);
		steps.put(35, zooPenguinTrust);

		steps.put(40, talkToZooPenguin);

		ConditionalStep lumbridgeVisit1 = new ConditionalStep(this, tuxedoTimeLumbridge);
		lumbridgeVisit1.addStep(new Conditions(isEmoting), emoteAtPenguinInLumbridge);
		lumbridgeVisit1.addStep(new Conditions(isPenguin, isAtLumbridgeSheepFarm), talkToThing);
		lumbridgeVisit1.addStep(new Conditions(isPenguin, isAtZoo), exitSuit);
		steps.put(45, lumbridgeVisit1);

		steps.put(50, returnToZooPenguin);

		steps.put(55, returnToThing);

		steps.put(60, fredTheFarmer);

		ConditionalStep outpostInfo = new ConditionalStep(this, stealCowbell);
		outpostInfo.addStep(cowbell, askThingAboutOutpost);
		steps.put(65, outpostInfo);

		ConditionalStep enterTheIceberg = new ConditionalStep(this, tellLarryAboutOutpost);
		enterTheIceberg.addStep(isEmoting, emoteAtPenguinOutpost);
		enterTheIceberg.addStep(isOnIceberg, kgpAgent);
		steps.put(70, enterTheIceberg);

		steps.put(75, noodle1);

		steps.put(80, noodle2);

		steps.put(85, kgpAgent2);

		ConditionalStep debriefSteps = new ConditionalStep(this, enterAvalanche);
		debriefSteps.addStep(isInPenguinRooms, kgpAgentInAvalanche);
		steps.put(90, debriefSteps);
		steps.put(95, debriefSteps);

		ConditionalStep agilityCourse = new ConditionalStep(this, enterAgilityCourse);
		agilityCourse.addStep(isInAgilityStart, agilityEnterWater);
		agilityCourse.addStep(isInAgilityWater, agilityExitWater);
		agilityCourse.addStep(isInAgilityStones, agilityJumpStones);
		agilityCourse.addStep(isInAgilityTreadSoftly, agilityTreadSoftly);
		agilityCourse.addStep(isInAgilityCrossIce, agilityCrossIce);
		agilityCourse.addStep(isInAgilityDone, agilityDone);
		steps.put(100, agilityCourse);

		steps.put(105, tellLarryAboutArmy);

		ConditionalStep goTalkToPingPong = new ConditionalStep(this, enterAvalanche2);
		goTalkToPingPong.addStep(isInPenguinRooms, pingPong1);
		steps.put(110, goTalkToPingPong);

		ConditionalStep gatherInstruments = new ConditionalStep(this, makeBongos);
		gatherInstruments.addStep(new Conditions(bongos, cowbell, isInPenguinRooms), pingPong2);
		gatherInstruments.addStep(new Conditions(bongos, cowbell), enterAvalanche3);
		gatherInstruments.addStep(bongos, stealCowbell);
		gatherInstruments.addStep(isPenguin, removePenguinSuitForBongos);
		steps.put(115, gatherInstruments);

		ConditionalStep enterControlRoom = new ConditionalStep(this, enterAvalanche3);
		enterControlRoom.addStep(isPreWarRoom, enterWarRoom);
		enterControlRoom.addStep(guardMoved, openControlDoor);
		enterControlRoom.addStep(isInPenguinRooms, pingPong3);
		steps.put(120, enterControlRoom);

		steps.put(125, killIcelords);

		ConditionalStep escape = new ConditionalStep(this, tellLarryPlans);
		escape.addStep(isInIcelordPit, exitIcelordPen);
		escape.addStep(isInIcelordRoom, useChasm);
		steps.put(130, escape);

		return steps;
	}

	@Override
	protected void setupRequirements()
	{
		oakPlanks = new ItemRequirement("Oak Planks (unnoted)", ItemID.PLANK_OAK, 10);
		oakPlankHighlight = new ItemRequirement("Oak Plank", ItemID.PLANK_OAK, 1);
		oakPlankHighlight.setHighlightInInventory(true);
		steelNails = new ItemRequirement("Steel Nails", ItemID.NAILS, 10);
		hammer = new ItemRequirement("Hammer", ItemCollections.HAMMER).isNotConsumed();
		spade = new ItemRequirement("Spade", ItemID.SPADE, 1).isNotConsumed();
		spadeHighlight = spade.highlighted();
		clockworkOrSteelBar = new ItemRequirement("Clockwork or Steel Bar", ItemID.STEEL_BAR);
		clockworkOrSteelBar.addAlternates(ItemID.POH_CLOCKWORK_MECHANISM);
		clockworkOrSteelBar.setDisplayMatchedItemName(true);
		steelBar = new ItemRequirement("Steel Bar", ItemID.STEEL_BAR);
		clockwork = new ItemRequirement("Clockwork", ItemID.POH_CLOCKWORK_MECHANISM, 1);
		plank = new ItemRequirement("Normal Plank", ItemID.WOODPLANK, 1);
		silk = new ItemRequirement("Silk", ItemID.SILK, 1);
		rawCodOrCharos = new ItemRequirement("Raw Cod", ItemID.RAW_COD).doNotAggregate();
		rawCodOrCharos.addAlternates(ItemID.RING_OF_CHAROS_UNLOCKED);
		rawCodOrCharos.setDisplayMatchedItemName(true);
		rawCodOrCharos.setTooltip("Ring of Charos (a) can also be used.");
		swampTar = new ItemRequirement("Swamp Tar", ItemID.SWAMP_TAR, 1);
		feathers = new ItemRequirement("Feathers", ItemID.FEATHER, 5);
		mahoganyPlank = new ItemRequirement("Mahogany Plank", ItemID.PLANK_MAHOGANY, 1);
		leather = new ItemRequirement("Leather", ItemID.LEATHER, 1);
		cowbell = new ItemRequirement("Cowbell", ItemID.PENG_COWBELL, 1);
		teleportLumbridge2 = new ItemRequirement("Teleports to Lumbridge", ItemID.POH_TABLET_LUMBRIDGETELEPORT, 2);
		teleportHouse = new ItemRequirement("Teleport to PoH with a clockwork table", ItemID.POH_TABLET_TELEPORTTOHOUSE,
			1);
		teleportArdougne = new ItemRequirement("Teleport to Ardougne", ItemID.POH_TABLET_ARDOUGNETELEPORT, 4);

		clockworkBookHighlight = new ItemRequirement("Clockwork book", ItemID.PENG_BOOK);
		clockworkBookHighlight.setHighlightInInventory(true);
		clockworkSuit = new ItemRequirement("Clockwork suit", ItemID.PENG_SUIT_UNWOUND);
		clockworkSuitHighlight = new ItemRequirement("Clockwork suit", ItemID.PENG_SUIT_UNWOUND);
		clockworkSuitHighlight.setHighlightInInventory(true);
		missionReportHighlight = new ItemRequirement("Mission report", ItemID.PENG_REPORT_1);
		missionReportHighlight.setHighlightInInventory(true);
		lumbridgeMissionReport = new ItemRequirement("Lumbridge mission report", ItemID.PENG_REPORT_2);
		mahoganyPlankHighlight = new ItemRequirement("Mahogany Plank", ItemID.PLANK_MAHOGANY);
		mahoganyPlankHighlight.setHighlightInInventory(true);
		leatherHighlight = new ItemRequirement("Leather", ItemID.LEATHER);
		leatherHighlight.setHighlightInInventory(true);
		bongos = new ItemRequirement("Penguin bongos", ItemID.PENG_BONGOS);
		kgpId = new ItemRequirement("Kgp id card", ItemID.PENG_ID);
		kgpId.setTooltip("You can get another from Noodle");
		combatGear = new ItemRequirement("Combat gear and food", -1, -1).isNotConsumed();
	}

	@Override
	protected void setupZones()
	{
		onIceberg = new Zone(new WorldPoint(2641, 3978, 1), new WorldPoint(2681, 4011, 1));
		inPenguinPen = new Zone(new WorldPoint(2592, 3267, 0), new WorldPoint(2597, 3271, 0));
		inPenguinPen2 = new Zone(new WorldPoint(2592, 3272, 0), new WorldPoint(2595, 3273, 0));
		atZoo = new Zone(new WorldPoint(2592, 3260, 0), new WorldPoint(2650, 3290, 0));
		atLumbridgeSheepFarm = new Zone(new WorldPoint(3170, 3253, 0), new WorldPoint(3215, 3285, 0));
		inAgilityStart = new Zone(new WorldPoint(2638, 4032, 1), new WorldPoint(2648, 4056, 1));
		inAgilityWater = new Zone(new WorldPoint(2628, 4053, 0), new WorldPoint(2635, 4065, 0));
		inAgilityStones = new Zone(new WorldPoint(2630, 4057, 1), new WorldPoint(2635, 4065, 1));
		inAgilityTreadSoftly = new Zone(new WorldPoint(2635, 4064, 1), new WorldPoint(2662, 4088, 1));
		inAgilityCrossIce = new Zone(new WorldPoint(2663, 4068, 1), new WorldPoint(2666, 4084, 1));
		inAgilityDone = new Zone(new WorldPoint(2652, 4035, 1), new WorldPoint(2666, 4042, 1));
		inPingPongRoom = new Zone(new WorldPoint(2664, 10394, 0), new WorldPoint(2672, 10399, 0));
		preWarRoom = new Zone(new WorldPoint(2641, 10410, 0), new WorldPoint(2671, 10419, 0));
		inIcelordPit = new Zone(new WorldPoint(2639, 10422, 0), new WorldPoint(2652, 10428, 0));
		inIcelordRoom = new Zone(new WorldPoint(2636, 10418, 0), new WorldPoint(2656, 10429, 0));
		inPenguinRooms = new Zone(new WorldPoint(2631, 10370, 0), new WorldPoint(2672, 10408, 0));
	}

	public void setupConditions()
	{
		isOnIceberg = new ZoneRequirement(onIceberg);
		birdHideBuilt = new VarbitRequirement(3294, 1);
		tableNearby = new Conditions(LogicType.OR,
			new ObjectCondition(ObjectID.POH_CLOCKMAKING_3),
			new ObjectCondition(ObjectID.POH_CLOCKMAKING_4));
		isPenguin = new VarbitRequirement(3306, 1);
		isInPenguinPen = new ZoneRequirement(inPenguinPen, inPenguinPen2);
		isEmoting = new VarbitRequirement(3308, 1);
		isAtZoo = new ZoneRequirement(atZoo);
		isAtLumbridgeSheepFarm = new ZoneRequirement(atLumbridgeSheepFarm);
		isInAgilityStart = new ZoneRequirement(inAgilityStart);
		isInAgilityWater = new ZoneRequirement(inAgilityWater);
		isInAgilityStones = new ZoneRequirement(inAgilityStones);
		isInAgilityTreadSoftly = new ZoneRequirement(inAgilityTreadSoftly);
		isInAgilityCrossIce = new ZoneRequirement(inAgilityCrossIce);
		isInAgilityDone = new ZoneRequirement(inAgilityDone);
		isInPingPongRoom = new ZoneRequirement(inPingPongRoom);
		isPreWarRoom = new ZoneRequirement(preWarRoom);
		isInIcelordPit = new ZoneRequirement(inIcelordPit);
		isInIcelordRoom = new ZoneRequirement(inIcelordRoom);
		isInPenguinRooms = new ZoneRequirement(inPenguinRooms);
		guardMoved = new VarbitRequirement(VarbitID.PENG_MULTI_KGP, 2, Operation.GREATER_EQUAL);
	}

	public void setupSteps()
	{
		talkToLarry = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Talk to Larry at the Ardougne Zoo.");
		talkToLarry.addDialogSteps("Yes.", "Okay, why not!");

		talkToLarryAgain = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Talk to Larry again.", oakPlanks, steelNails, hammer, spade);
		talkToLarryAgain.addDialogStep("Yes, I have all the materials.");
		talkToLarryAgain.addDialogStep("Yes");

		usePlankOnFirmSnow = new ObjectStep(this, ObjectID.PENG_OBSERVER_CABIN_MULTILOC, new WorldPoint(2666, 3991, 1), "Use an oak plank on the firm snow patch.", oakPlankHighlight);
		usePlankOnFirmSnow.addIcon(ItemID.PLANK_OAK);

		useSpadeOnBirdHide = new ObjectStep(this, ObjectID.PENG_OBSERVER_CABIN_MULTILOC, new WorldPoint(2666, 3991, 1), "Use a spade on the bird hide to cover it in snow.", spadeHighlight);
		useSpadeOnBirdHide.addIcon(ItemID.SPADE);

		learnPenguinEmotes = new NpcStep(this, NpcID.PENG_LARRY_ICE, new WorldPoint(2670,3988,1), "Talk to Larry on the iceberg to enter the bird hide.");


		talkToLarryAfterEmotes = new NpcStep(this, NpcID.PENG_LARRY_ICE, new WorldPoint(2670,3988,1), "Talk to Larry again.");
		talkToLarryAfterEmotes.addDialogStep("That's crazy!");

		returnToRelleka = new ObjectStep(this, ObjectID.PENG_ROW_BOAT_CLICKZONE, "Click the boat to return to Relleka.");

		talkToLarryInRelleka = new NpcStep(this, NpcID.PENG_LARRY_RELL, new WorldPoint(2707,3732,0), "Talk to Larry in Relleka.");

		enterPoh = new DetailedQuestStep(this, "Travel to your POH or another POH with a Crafting table 3 or 4.", clockworkOrSteelBar, plank, silk);

		makeClockwork = new ObjectStep(this, ObjectID.POH_CLOCKMAKING_3, "Craft a steel bar into a clockwork at a crafting table 3.", steelBar);
		makeClockwork.addDialogStep("Clockwork mechanism");
		((ObjectStep) makeClockwork).addAlternateObjects(ObjectID.POH_CLOCKMAKING_4);

		makePenguin = new ObjectStep(this, ObjectID.POH_CLOCKMAKING_3, "Craft a clockwork penguin at a crafting table 3 or 4.", clockwork, plank, silk);
		makePenguin.addDialogStep("Clockwork toy");
		makePenguin.addDialogStep("Clockwork penguin");
		((ObjectStep) makePenguin).addAlternateObjects(ObjectID.POH_CLOCKMAKING_4);

		bringSuitToLarry = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Talk to Larry back in the Ardougne Zoo.");
		bringSuitToLarry.addDialogStep("Yes, I have it.");
		bringSuitToLarry.addDialogStep("Yes");

		talkToLarryOnIcebergWithSuit = new NpcStep(this, NpcID.PENG_LARRY_ICE, new WorldPoint(2670,3988,1), "Talk to Larry on the iceberg.");
		talkToLarryOnIcebergWithSuit.addDialogSteps("It looks like a warning message to keep us away.", "Yes");

		readMissionReport = new DetailedQuestStep(this, "Read the mission report.", missionReportHighlight);

		tuxedoTime = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Unequip cape and weapons and right-click Tuxedo-time Larry at the Ardougne Zoo.");
		tuxedoTime.addDialogStep("Yes");

		enterPenguinPen = new ObjectStep(this, ObjectID.PENG_ARDOUGNE_ENCLOSURE_DOOR, new WorldPoint(2594, 3266, 0), "Enter the penguin pen.");

		talkToZooPenguin = new NpcStep(this, NpcID.PENG_ZOO, new WorldPoint(2596, 3270, 0), "Talk to the zoo penguin.");

		emoteAtPenguin = new PuzzleWrapperStep(this, new PenguinEmote(this), "Perform the correct series of emotes to the penguin.");

		exitSuit = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Talk to Larry to exit the penguin suit.");
		talkToLarryMissionReport = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(2597, 3266, 0), "Talk to Larry about the mission report, then travel to the sheep farm in Lumbridge.");
		talkToLarryMissionReport.addSubSteps(exitSuit);

		tuxedoTimeLumbridge = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(3212, 3263, 0), "Tuxedo-time Larry in the Lumbridge sheep farm.");

		talkToThing = new NpcStep(this, NpcID.SHEEP_SHEARER_THE_THING,new WorldPoint(3201, 3266, 0), "Talk to the penguins disguised as a sheep in the Lumbridge sheep farm. You will need to use the same 3 emotes as the penguin from the bird hide cutscene.");

		emoteAtPenguinInLumbridge = new PuzzleWrapperStep(this, new PenguinEmote(this), "Perform the correct series of emotes to the penguin.");

		returnToZooPenguin = new NpcStep(this, NpcID.PENG_ZOO, new WorldPoint(2596, 3270, 0), "Return to the Ardougne Zoo penguin with either a raw cod, or wearing the ring of charos.", rawCodOrCharos);
		returnToZooPenguin.addDialogSteps(
			"I need that phrase!", "The penguins in Lumbridge refuse to talk to me.",
			"I must have left the outpost before they gave out the phrase.", "Sure!");

		returnToThing = new NpcStep(this, NpcID.SHEEP_SHEARER_THE_THING, new WorldPoint(3201, 3266, 0), "Speak to the sheep-penguins again in the Lumbridge sheep farm.", clockworkSuit);

		fredTheFarmer = new NpcStep(this, NpcID.FRED_THE_FARMER, new WorldPoint(3189, 3273, 0), "Talk to Fred the Farmer without the penguin suit.");
		fredTheFarmer.addDialogStep("I need to talk to you about penguins.");
		fredTheFarmer.addDialogStep("Bully Fred");

		stealCowbell = new ObjectStep(this, ObjectID.FAT_COW, new WorldPoint(3172, 3318, 0), "Steal a cowbell from a dairy cow.");

		askThingAboutOutpost = new NpcStep(this, NpcID.SHEEP_SHEARER_THE_THING, new WorldPoint(3201, 3266, 0), "Speak to the sheep-penguins once again in the Lumbridge sheep farm to learn about the outpost.", clockworkSuit);
		askThingAboutOutpost.addDialogStep("The Farmer is harmless.");

		tellLarryAboutOutpost = new NpcStep(this, NpcID.PENG_LARRY_ZOO, new WorldPoint(3212, 3263, 0), "Tell Larry about the outpost and travel back to the iceberg.", swampTar, feathers, lumbridgeMissionReport);
		tellLarryAboutOutpost.addDialogStep("Yes");

		kgpAgent = new NpcStep(this, NpcID.PENG_KGP, new WorldPoint(2639, 4008, 1), "Jump in the penguin suit and talk to the KGP Agent on the north west portion of the iceberg.");

		emoteAtPenguinOutpost = new PenguinEmote(this);

		noodle1 = new NpcStep(this, NpcID.PENG_NOODLE, new WorldPoint(2644,4008,1),"Talk to Noodle.");

		noodle2 = new NpcStep(this, NpcID.PENG_NOODLE,new WorldPoint(2644,4008,1), "Give Noodle the swamp tar to get an ID card and a mission report.", swampTar, feathers);
		noodle2.addDialogStep("Yeah, I got it.");

		kgpAgent2 = new NpcStep(this, NpcID.PENG_KGP, new WorldPoint(2639, 4008, 1), "Talk to the KGP Agent again.", kgpId);
		enterAvalanche = new ObjectStep(this, ObjectID.PENG_AVAL_L, new WorldPoint(2638,4011,1),"Enter the avalanche.");
		enterAvalanche2 = new ObjectStep(this, ObjectID.PENG_AVAL_L, new WorldPoint(2638,4011,1),"Enter the avalanche as a penguin.", clockworkSuit.equipped());
		enterAvalanche3 = new ObjectStep(this, ObjectID.PENG_AVAL_L, new WorldPoint(2638,4011,1),"Enter the avalanche as a penguin.", clockworkSuit.equipped());

		kgpAgentInAvalanche = new NpcStep(this, NpcID.PENG_KGP, new WorldPoint(2647, 10384, 0), "Talk to the KGP Agent in the first room to the west of the entrance.");
		((NpcStep) kgpAgentInAvalanche).setMaxRoamRange(12);
		enterAgilityCourse = new ObjectStep(this, ObjectID.PENG_BASE_DOUBLE_DOOR_MID_AGILITY, new WorldPoint(2633, 10404, 0), "Enter the door to the west of the KGP Agent to begin the agility course.");

		agilityCourse = new DetailedQuestStep(this, "Complete the agility course.");
		agilityEnterWater = new TileStep(this, new WorldPoint(2636, 4054, 1), "Walk up to the start of the agility course.");
		agilityExitWater = new ObjectStep(this, ObjectID.PENG_AGILITY_CRUSHCOURSE_STEPSTONE01, new WorldPoint(2630, 4057, 0), "Avoid the ice and exit the water.");
		agilityJumpStones = new TileStep(this, new WorldPoint(2635, 4065, 1), "Jump across the stepping stones.");
		agilityTreadSoftly = new TileStep(this, new WorldPoint(2663, 4082, 1), "Tread-softly across the icicles.");
		agilityCrossIce = new TileStep(this, new WorldPoint(2664, 4068, 1), "Cross ice to get up the hill.");
		agilityDone = new NpcStep(this, NpcID.PENG_AGILITY_INSTRUCTOR, "Talk to the Agility Instructor.");
		agilityCourse.addSubSteps(enterAgilityCourse, agilityEnterWater, agilityExitWater, agilityJumpStones, agilityTreadSoftly, agilityCrossIce, agilityDone);

		tellLarryAboutArmy = new NpcStep(this, NpcID.PENG_LARRY_ICE,  new WorldPoint(2670,3988,1), "Return to Larry to tell him about the penguin army. You can quickly return to him by removing the penguin suit.");

		kgpBeforePingPong = new NpcStep(this, NpcID.PENG_KGP, new WorldPoint(2655, 10408, 0), "Re-enter the outpost and talk to the KGP agent north of the entrance.");
		((NpcStep) kgpBeforePingPong).setMaxRoamRange(2);

		pingPong1 = new NpcStep(this, NpcID.PENG_PING, new WorldPoint(2668,10396,0), "Talk to Ping or Pong in the room to the east.");
		((NpcStep) pingPong1).addAlternateNpcs(NpcID.PENG_PONG);

		removePenguinSuitForBongos = new DetailedQuestStep(this, "Remove the penguin suit.");

		makeBongos = new DetailedQuestStep(this, "Use the mahogany plank on the leather to make bongos.", mahoganyPlankHighlight, leatherHighlight);
		makeBongos.addSubSteps(removePenguinSuitForBongos);

		pingPong2 = new NpcStep(this, NpcID.PENG_PING, new WorldPoint(2668,10396,0), "Return to Ping or Pong to give them the bongos and cowbells.", bongos, cowbell);
		((NpcStep) pingPong2).addAlternateNpcs(NpcID.PENG_PONG);
		pingPong2.addDialogStep("Yes.");
		pingPong3 = new NpcStep(this, NpcID.PENG_PING, new WorldPoint(2668,10396,0), "Return to Ping or Pong.");
		((NpcStep) pingPong3).addAlternateNpcs(NpcID.PENG_PONG);
		pingPong3.addDialogStep("Yes.");
		pingPong2.addSubSteps(enterAvalanche3, pingPong3);

		openControlDoor = new ObjectStep(this, ObjectID.PENG_BASE_BOOTH_FRONT, "Open the control room doors via the control panel.");

		enterWarRoom = new ObjectStep(this, ObjectID.PENG_BASE_DOOR, new WorldPoint(2671, 10418, 0), "Enter the war room and walk a few steps in to be captured.");

		killIcelords = new NpcStep(this, NpcID.PENG_ICELORD_WARRIOR01, new WorldPoint(2647, 10425, 0), "Kill icelords until you are able to leave through the door to the west. May take up to 3 kills.", true);
		((NpcStep) killIcelords).addAlternateNpcs(NpcID.PENG_ICELORD_WARRIOR02, NpcID.PENG_ICELORD_WARRIOR03, NpcID.PENG_ICELORD_WARRIOR04);
		exitIcelordPen = new ObjectStep(this, ObjectID.PENG_ICELORD_PEN_DOOR, new WorldPoint(2639, 10424, 0), "Leave through the door to the west.");
		killIcelords.addSubSteps(exitIcelordPen);

		useChasm = new ObjectStep(this, ObjectID.PENG_BASE_CHASM, new WorldPoint(2657, 10423, 0), "Use the chasm to exit the cave.");

		tellLarryPlans = new NpcStep(this, NpcID.PENG_LARRY_ICE,  new WorldPoint(2670,3988,1), "Return to Larry to finish the quest.");
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(oakPlanks, steelNails, hammer, spade, clockworkOrSteelBar, plank, silk, rawCodOrCharos, swampTar, feathers, mahoganyPlank, leather);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		ArrayList<ItemRequirement> reqs = new ArrayList<>();
		reqs.add(teleportArdougne);
		reqs.add(teleportHouse);
		reqs.add(teleportLumbridge2);
		reqs.add(combatGear);
		return reqs;
	}

	@Override
	public List<String> getCombatRequirements()
	{
		ArrayList<String> reqs = new ArrayList<>();
		reqs.add("1-3 Icelords (level 51)");
		return reqs;
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return Arrays.asList(
				new ExperienceReward(Skill.CRAFTING, 2000),
				new ExperienceReward(Skill.AGILITY, 5000),
				new ExperienceReward(Skill.CONSTRUCTION, 1500),
				new ExperienceReward(Skill.ATTACK, 40));
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return Arrays.asList(
				new UnlockReward("Ability to create Penguin Suits"),
				new UnlockReward("Ability to use the Penguin Agility Course"),
				new UnlockReward("Abillity to make Bongo Drums"));
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();

		allSteps.add(new PanelDetails("Larry's Story", Arrays.asList(talkToLarry, talkToLarryAgain), oakPlanks, steelNails, hammer, spade));
		allSteps.add(new PanelDetails("Penguin Stake-out", Arrays.asList(usePlankOnFirmSnow, useSpadeOnBirdHide, learnPenguinEmotes, talkToLarryAfterEmotes, returnToRelleka), oakPlanks, steelNails, hammer, spade));
		allSteps.add(new PanelDetails("A Clockwork Penguin", Arrays.asList(talkToLarryInRelleka, enterPoh, makeClockwork, makePenguin, bringSuitToLarry, talkToLarryOnIcebergWithSuit), plank, clockworkOrSteelBar, silk));
		allSteps.add(new PanelDetails("Ardougne Mission Report", Arrays.asList(tuxedoTime, enterPenguinPen, talkToZooPenguin, talkToLarryMissionReport), clockworkSuit));
		allSteps.add(new PanelDetails("Lumbridge Mission Report", Arrays.asList(tuxedoTimeLumbridge, talkToThing, returnToZooPenguin, returnToThing, fredTheFarmer, stealCowbell, askThingAboutOutpost), clockworkSuit, rawCodOrCharos, feathers, swampTar, mahoganyPlank, leather, combatGear));
		allSteps.add(new PanelDetails("Penguin Outpost", Arrays.asList(tellLarryAboutOutpost, kgpAgent, noodle1, noodle2, kgpAgent2, enterAvalanche), clockworkSuit, feathers, swampTar, mahoganyPlank, leather, combatGear));
		allSteps.add(new PanelDetails("Briefing and Agility", Arrays.asList(kgpAgentInAvalanche, agilityCourse, tellLarryAboutArmy), clockworkSuit, mahoganyPlank, leather, combatGear));
		allSteps.add(new PanelDetails("Musical Penguins", Arrays.asList(pingPong1, makeBongos, pingPong2), clockworkSuit, mahoganyPlank, leather,
				combatGear));
		allSteps.add(new PanelDetails("The War Room", Arrays.asList(openControlDoor, enterWarRoom, killIcelords, useChasm, tellLarryPlans), clockworkSuit, combatGear));

		return allSteps;
	}

	@Override
	public List<Requirement> getGeneralRecommended()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new ItemRequirement("Access to a Crafting Table 3", -1));
		return req;
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new SkillRequirement(Skill.HUNTER, 10));
		req.add(new SkillRequirement(Skill.AGILITY, 30));
		req.add(new SkillRequirement(Skill.CRAFTING, 30));
		req.add(new SkillRequirement(Skill.CONSTRUCTION, 34));
		req.add(new SkillRequirement(Skill.THIEVING, 15));
		return req;
	}
}
