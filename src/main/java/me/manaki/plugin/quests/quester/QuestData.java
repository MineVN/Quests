package me.manaki.plugin.quests.quester;

public class QuestData {

    private final String id;
    private int stage;
    private int stageCount;
    private long takeTime;

    public QuestData(String id) {
        this.id = id;
        this.stage = 0;
        this.stageCount = 0;
        this.takeTime = System.currentTimeMillis();
    }

    public QuestData(String id, int stage, int stageCount) {
        this.id = id;
        this.stage = stage;
        this.stageCount = stageCount;
    }

    public String getID() {
        return id;
    }

    public int getStage() {
        return stage;
    }

    public int getStageCount() {
        return stageCount;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void setStageCount(int stageCount) {
        this.stageCount = stageCount;
    }

    public long getTakeTime() {
        return takeTime;
    }
}
