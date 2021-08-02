Last update: 01/08/2021

**Quest
- name
- desc
- reward-desc
- reward-commands
- requirement-desc
- requirements
- stages

**Requirement
- QUEST_DONE
- DATE
- LEVEL

**Stage
- type
- objective
- tip
- count
- data
- on-count-added-commands
- on-start-commands
- on-end-commands

**Available requirement types:
- QUEST_DONE
    + Value: String type
- DATE
    + Value: Date type (dd/mm/yyyy - allow comparison)
    + Example: > 20/10/2020
- LEVEL
    + Value: Number type

**Available stage types:
- PLACEHOLDER_CHANGE
    + *count: added value
    + *placeholder: String type
- DUNGEON_FINISH
    + count: times
    + dungeon-id: String type
    + result: String type
    + mem-start: Number type
    + mem-finish: Number type
    + mob-kills: Number type
    + slave-saves: Number type
    + death-times: Number type
    + play-time: Number type
- DUNGEON_MOB_KILL
    + count: amount
    + dungeon-id: String type
    + mob-id: String type
- SORA_ITEM_ASCENT
    + ascent-level: Number type
- SORA_ITEM_ENHANCE
    + enhance-level: Number type
- SORA_ITEM_UPGRADE
    + upgrade-level: Number type
- SORA_ITEM_CRAFT
    + recipe-id: String type
- SHOPS_ITEM_DELIVER
    + count: amount
    + item-id: Number type
    + npc-id: Number type
- CONVERSATION
    + count: amount
    + npc-id: Number type
    + c-0 -> c-(amount-1): Conversation
- SORA_WISH
    + wish: String type
- LOCATION_REACH
    + location: String type (world;x;y;z)
    + radius: Number type
- COMMAND_EXECUTE
    + command: String type
- BLOCK_BREAK
    + block-type: String type
    + world: String type
- MYTHICMOB_KILL
    + mythicmob-id: String type
- MARKET_SELL:
    + item-material: String type


**Available command types:
- opplayercmd
    + Example: {5} [opplayercmd] tell Hello
- playercmd
    + Example: {5} [playercmd] tell Hello
- consolecmd
    + Example: {5} [consolecmd] tell Hello
- message
    + Example: {5} [message] &aWelcome to this week's worldy wise
- title(title;subtitle)
    + Example: {10} [title] This is a title;And this is a subtitle
- broadcast
    + Example: {5} [broadcast] &aHi
    + Shouldnt use it, use [message] instead
- sound(sound;f1;f2)
    + Example: {5} [sound] ENTITY_FIREWORK_ROCKET_LAUCH;1;1

**Command placeholders:
- %quest_name%
- %quest_desc%
- %quest_points%
- %data_stage_count%
- %stage_objective%
- %stage_tip%
- %stage_count%
- %stage_data_<data>% ex: %stage_data_npcid%

**Value types
- String type:
    + *: all
    + and;v1;v2;v3: v1 and v2
    + or;v1;v2;v3: v1 or v2 or v3
- Number type:
    + 0: =0
    + >0: >0
    + <, <=, >= are similar as >

**PlaceholderAPI
- %quests_main_quest_name%
- %quests_main_quest_stage%
- %quests_main_quest_objective_1%
- %quests_main_quest_objective_2%
- %quests_main_quest_tip_1%
- %quests_main_quest_tip_2%
