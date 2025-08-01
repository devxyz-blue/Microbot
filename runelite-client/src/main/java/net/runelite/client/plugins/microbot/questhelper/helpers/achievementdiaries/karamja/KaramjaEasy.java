/*
 * Copyright (c) 2021, Zoinkwiz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.microbot.questhelper.helpers.achievementdiaries.karamja;

import net.runelite.client.plugins.microbot.questhelper.bank.banktab.BankSlotIcons;
import net.runelite.client.plugins.microbot.questhelper.collections.ItemCollections;
import net.runelite.client.plugins.microbot.questhelper.panel.PanelDetails;
import net.runelite.client.plugins.microbot.questhelper.questhelpers.ComplexStateQuestHelper;
import net.runelite.client.plugins.microbot.questhelper.requirements.Requirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.conditional.Conditions;
import net.runelite.client.plugins.microbot.questhelper.requirements.item.ItemRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.player.SkillRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.LogicType;
import net.runelite.client.plugins.microbot.questhelper.requirements.util.Operation;
import net.runelite.client.plugins.microbot.questhelper.requirements.var.VarbitRequirement;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.Zone;
import net.runelite.client.plugins.microbot.questhelper.requirements.zone.ZoneRequirement;
import net.runelite.client.plugins.microbot.questhelper.rewards.ItemReward;
import net.runelite.client.plugins.microbot.questhelper.rewards.UnlockReward;
import net.runelite.client.plugins.microbot.questhelper.steps.*;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KaramjaEasy extends ComplexStateQuestHelper
{
	// Items required
	ItemRequirement pickaxe, coins, smallFishingNet, combatGear;

	// Items recommended
	ItemRequirement food, antipoison;

	ItemRequirement seaweed;

	Requirement notSwungOnRope, notPickedBananas, notMinedGold, notGoneToSarim, notGoneToArdougne,
		notGoneToCairn, notFished, notPickedUpSeaweed, notEnteredFightCave, notKilledJogre;

	QuestStep swingRope, pickBananas, mineGold, goSarim, goArdougne, goCairn, goFish, pickupSeaweed, enterCave,
		enterTzhaar, enterFightCave, enterPothole, killJogre, claimReward;

	Zone cave, tzhaar, pothole;

	ZoneRequirement inCave, inTzhaar, inPothole;

	ConditionalStep swungOnRopeTask, pickedBananasTask, minedGoldTask, goneToSarimTask, goneToArdougneTask,
		goneToCairnTask, fishedTask, pickedUpSeaweedTask, enteredFightCaveTask, killedJogreTask;

	@Override
	public QuestStep loadStep()
	{
		initializeRequirements();
		setupSteps();

		ConditionalStep doEasy = new ConditionalStep(this, claimReward);

		goneToSarimTask = new ConditionalStep(this, goSarim);
		doEasy.addStep(notGoneToSarim, goneToSarimTask);

		pickedBananasTask = new ConditionalStep(this, pickBananas);
		doEasy.addStep(notPickedBananas, pickedBananasTask);

		fishedTask = new ConditionalStep(this, goFish);
		doEasy.addStep(notFished, fishedTask);

		enteredFightCaveTask = new ConditionalStep(this, enterCave);
		enteredFightCaveTask.addStep(inCave, enterFightCave);
		enteredFightCaveTask.addStep(inTzhaar, enterTzhaar);
		doEasy.addStep(notEnteredFightCave, enteredFightCaveTask);

		goneToArdougneTask = new ConditionalStep(this, goArdougne);
		doEasy.addStep(notGoneToArdougne, goneToArdougneTask);

		minedGoldTask = new ConditionalStep(this, mineGold);
		doEasy.addStep(notMinedGold, minedGoldTask);

		swungOnRopeTask = new ConditionalStep(this, swingRope);
		doEasy.addStep(notSwungOnRope, swungOnRopeTask);

		pickedUpSeaweedTask = new ConditionalStep(this, pickupSeaweed);
		doEasy.addStep(notPickedUpSeaweed, pickedUpSeaweedTask);

		goneToCairnTask = new ConditionalStep(this, goCairn);
		doEasy.addStep(notGoneToCairn, goneToCairnTask);

		killedJogreTask = new ConditionalStep(this, enterPothole);
		killedJogreTask.addStep(inPothole, killJogre);
		doEasy.addStep(notKilledJogre, killedJogreTask);

		return doEasy;
	}

	@Override
	protected void setupRequirements()
	{
		seaweed = new ItemRequirement("Seaweed", ItemID.SEAWEED);

		notPickedBananas = new VarbitRequirement(VarbitID.ATJUN_EASY_BANANA, 4, Operation.LESS_EQUAL);
		notSwungOnRope = new VarbitRequirement(3567, 0);
		notMinedGold = new VarbitRequirement(3568, 0);
		notGoneToSarim = new VarbitRequirement(3569, 0);
		notGoneToArdougne = new VarbitRequirement(3570, 0);
		notGoneToCairn = new VarbitRequirement(3571, 0);
		notFished = new VarbitRequirement(3572, 0);
		notPickedUpSeaweed = new VarbitRequirement(VarbitID.ATJUN_EASY_SEAWEED, 4, Operation.LESS_EQUAL);
		notEnteredFightCave = new VarbitRequirement(3574, 0);
		notKilledJogre = new VarbitRequirement(3575, 0);

		pickaxe = new ItemRequirement("Any pickaxe", ItemCollections.PICKAXES).showConditioned(notMinedGold).isNotConsumed();
		coins = new ItemRequirement("Coins", ItemCollections.COINS).showConditioned(new Conditions(LogicType.OR,
			notGoneToSarim, notGoneToArdougne));
		smallFishingNet = new ItemRequirement("Small fishing net", ItemID.NET).showConditioned(notFished).isNotConsumed();
		combatGear = new ItemRequirement("Combat gear to defeat a Jogre (level 56)", -1, -1).showConditioned(notKilledJogre).isNotConsumed();
		combatGear.setDisplayItemId(BankSlotIcons.getCombatGear());

		food = new ItemRequirement("Food", ItemCollections.GOOD_EATING_FOOD, -1);
		antipoison = new ItemRequirement("Antipoison", ItemCollections.ANTIPOISONS, -1);

		inCave = new ZoneRequirement(cave);
		inTzhaar = new ZoneRequirement(tzhaar);
		inPothole = new ZoneRequirement(pothole);
	}

	@Override
	protected void setupZones()
	{
		cave = new Zone(new WorldPoint(2821, 9545, 0), new WorldPoint(2879, 9663, 0));
		tzhaar = new Zone(new WorldPoint(2360, 5056, 0), new WorldPoint(2560, 5185, 0));
		pothole = new Zone(new WorldPoint(2824, 9462, 0), new WorldPoint(2883, 9533, 0));
	}

	public void setupSteps()
	{
		swingRope = new ObjectStep(this, ObjectID.TREE_ROPESWING1, new WorldPoint(2708, 3209, 0), "Swing on the " +
			"ropeswing west of Brimhaven.");
		pickBananas = new ObjectStep(this, ObjectID.BANANATREEFULL, new WorldPoint(2916, 3162, 0),
			"Pick 5 bananas from the banana plantation.");
		((ObjectStep) pickBananas).addAlternateObjects(ObjectID.BANANATREEFOUR, ObjectID.BANANATREETHREE,
			ObjectID.BANANATREETWO, ObjectID.BANANATREEONE);
		mineGold = new ObjectStep(this, ObjectID.GOLDROCK2, new WorldPoint(2732, 3223, 0),
			"Mine a gold rock north west of Brimhaven.", pickaxe);
		goSarim = new NpcStep(this, NpcID.CUSTOMS_OFFICER, new WorldPoint(2954, 3147, 0),
			"Travel to Port Sarim from Musa Point with the Customs officer.", coins.quantity(30));
		goArdougne = new NpcStep(this, NpcID.CAPTAIN_BARNABY_KARAMJA, new WorldPoint(2772, 3228, 0),
			"Travel to Ardougne from Brimhaven with Captain Barnaby.", coins.quantity(30));
		goCairn = new DetailedQuestStep(this, new WorldPoint(2770, 2979, 0),
			"Travel to Cairn Island by climbing up the rocks east of it, then crossing the bridge.");
		goFish = new NpcStep(this, NpcID._0_45_49_SALTFISH, new WorldPoint(2924, 3178, 0),
			"Fish north of the banana plantation.", true, smallFishingNet);
		pickupSeaweed = new DetailedQuestStep(this, new WorldPoint(2756, 3125, 0),
			"Pick up 5 seaweed on Karamja's coast. You can just drop a piece and pick it up again 5 times.", seaweed);
		enterCave = new ObjectStep(this, ObjectID.VOLCANO_ENTRANCE, new WorldPoint(2857, 3169, 0),
			"Enter the fight pits or fight caves in Mor Ul Rek under the Karamja Volcano.");
		enterTzhaar = new ObjectStep(this, ObjectID.TZHAAR_KARAMJADUNGEON_WALL_ENTRANCE, new WorldPoint(2864, 9572, 0),
			"Enter the fight pits or fight caves in Mor Ul Rek under the Karamja Volcano.");
		enterFightCave = new ObjectStep(this, ObjectID.TZHAAR_FIGHTCAVE_WALL_ENTRANCE, new WorldPoint(2438, 5167, 0),
			"Enter the fight pits or fight caves in Mor Ul Rek under the Karamja Volcano.");
		enterFightCave.addSubSteps(enterCave, enterTzhaar);
		enterPothole = new ObjectStep(this, ObjectID.POTHOLE_CAVE_ENTRANCE, new WorldPoint(2825, 3119, 0),
			"Enter the dungeon east of Tai Bwo Wannai, and kill a Jogre in there.", combatGear);
		enterPothole.addDialogStep("Yes, I'll enter the cave.");
		killJogre = new NpcStep(this, NpcID.JOGRE, new WorldPoint(2832, 9517, 0),
			"Kill a Jogre.", true, combatGear);
		enterPothole.addSubSteps(killJogre);
		claimReward = new NpcStep(this, NpcID.AGILITYARENA_TICKETTRADER_2OPS, new WorldPoint(2810, 3192, 0),
			"Talk to Pirate Jackie the Fruit in Brimhaven to claim your reward!");
		claimReward.addDialogStep("I have a question about my Achievement Diary.");
	}

	@Override
	public List<ItemRequirement> getItemRequirements()
	{
		return Arrays.asList(pickaxe, coins.quantity(120), smallFishingNet, combatGear);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return Arrays.asList(food, antipoison);
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		ArrayList<Requirement> req = new ArrayList<>();
		req.add(new SkillRequirement(Skill.AGILITY, 15, true));
		req.add(new SkillRequirement(Skill.MINING, 40, true));
		return req;
	}

	@Override
	public List<ItemReward> getItemRewards()
	{
		return Arrays.asList(
			new ItemReward("Karamja Gloves (1)", ItemID.ATJUN_GLOVES_EASY, 1),
			new ItemReward("1,000 Exp. Lamp (Any skill)", ItemID.THOSF_REWARD_LAMP, 1));
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return Arrays.asList(
			new UnlockReward("Better prices in shops on Karamja and in Tzhaar City."),
			new UnlockReward("Half price ships from Ardougne to Brimhaven and Musa Point to Port Sarim."));
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		List<PanelDetails> allSteps = new ArrayList<>();

		PanelDetails travelSarimSteps = new PanelDetails("Travel to Port Sarim", Collections.singletonList(goSarim),
			coins.quantity(30));
		travelSarimSteps.setDisplayCondition(notGoneToSarim);
		travelSarimSteps.setLockingStep(goneToSarimTask);
		allSteps.add(travelSarimSteps);

		PanelDetails pickBananasSteps = new PanelDetails("Pick 5 Bananas", Collections.singletonList(pickBananas));
		pickBananasSteps.setDisplayCondition(notPickedBananas);
		pickBananasSteps.setLockingStep(pickedBananasTask);
		allSteps.add(pickBananasSteps);

		PanelDetails goFishSteps = new PanelDetails("Fish North of Banana Plantation", Collections.singletonList(goFish),
			smallFishingNet);
		goFishSteps.setDisplayCondition(notFished);
		goFishSteps.setLockingStep(fishedTask);
		allSteps.add(goFishSteps);

		PanelDetails enterFightCaveSteps = new PanelDetails("Attempt the Fight Cave or TzHaar Fight Pits", Collections.singletonList(enterFightCave));
		enterFightCaveSteps.setDisplayCondition(notEnteredFightCave);
		enterFightCaveSteps.setLockingStep(enteredFightCaveTask);
		allSteps.add(enterFightCaveSteps);

		PanelDetails goArdougneSteps = new PanelDetails("Travel to Ardougne", Collections.singletonList(goArdougne),
			coins.quantity(30));
		goArdougneSteps.setDisplayCondition(notGoneToArdougne);
		goArdougneSteps.setLockingStep(goneToArdougneTask);
		allSteps.add(goArdougneSteps);

		PanelDetails mineGoldSteps = new PanelDetails("Mine gold", Collections.singletonList(mineGold),
			new SkillRequirement(Skill.MINING, 40, true), pickaxe);
		mineGoldSteps.setDisplayCondition(notMinedGold);
		mineGoldSteps.setLockingStep(minedGoldTask);
		allSteps.add(mineGoldSteps);

		PanelDetails swingRopeSteps = new PanelDetails("Swing Rope to Moss Giant Isle", Collections.singletonList(swingRope),
			new SkillRequirement(Skill.AGILITY, 10, true));
		swingRopeSteps.setDisplayCondition(notSwungOnRope);
		swingRopeSteps.setLockingStep(swungOnRopeTask);
		allSteps.add(swingRopeSteps);

		PanelDetails pickupSeaweedSteps = new PanelDetails("Pickup 5 Seaweed", Collections.singletonList(pickupSeaweed));
		pickupSeaweedSteps.setDisplayCondition(notPickedUpSeaweed);
		pickupSeaweedSteps.setLockingStep(pickedUpSeaweedTask);
		allSteps.add(pickupSeaweedSteps);

		PanelDetails goCairnSteps = new PanelDetails("Explore Cairn Isle", Collections.singletonList(goCairn),
			new SkillRequirement(Skill.AGILITY, 15, true));
		goCairnSteps.setDisplayCondition(notGoneToCairn);
		goCairnSteps.setLockingStep(goneToCairnTask);
		allSteps.add(goCairnSteps);

		PanelDetails killJogreSteps = new PanelDetails("Kill Jogre in Pothole Dungeon", Collections.singletonList(enterPothole));
		killJogreSteps.setDisplayCondition(notKilledJogre);
		killJogreSteps.setLockingStep(killedJogreTask);
		allSteps.add(killJogreSteps);

		PanelDetails finishOffSteps = new PanelDetails("Finishing off", Collections.singletonList(claimReward));
		allSteps.add(finishOffSteps);

		return allSteps;
	}
}
