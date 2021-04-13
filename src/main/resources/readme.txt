Last update: 13/04/2021

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

**Value
- String type:
    + *: all
    + and;v1;v2;v3: v1 and v2
    + or;v1;v2;v3: v1 or v2 or v3
- Number type:
    + 0: =0
    + >0: >0
    + <, <=, >= are similar as >

*PlaceholderAPI
%quests_main_quest_name%
%quests_main_quest_stage%
%quests_main_quest_objective_1%
%quests_main_quest_objective_2%
%quests_main_quest_tip_1%
%quests_main_quest_tip_2%