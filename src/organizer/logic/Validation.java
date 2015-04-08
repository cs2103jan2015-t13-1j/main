package organizer.logic;

//@author A0098824N
public class Validation {
    private static final String rankPattern = "high|medium|low|remove";
    
    boolean isSearch, isView;
    
    public Validation() {
        this.isSearch = false;
        this.isView = false;
    }
    
    public boolean getIsView() {
        return isView;
    }
    
    public void setIsView(boolean isView) {
        this.isView = isView;
    }
    
    public boolean getIsSearch() {
        return isSearch;
    }
    
    public void setIsSearch(boolean isSearch) {
        this.isSearch = isSearch;
    }
    
    public boolean isValidTask(int lineNum, TaskListSet allList) {
        if(getIsSearch() && lineNum > allList.getResultList().size()) {
            return false;
        } else if(getIsView() && lineNum > allList.getViewList().size()) {
            return false;
        } else if(lineNum > allList.getTaskList().size()){
            return false;
        } else if(lineNum <= 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public int checkForTaskID(int lineNum, TaskListSet allList) {
        int taskID = -1;
        if(isSearch) {
            taskID = allList.getResultList().get(lineNum-1).getTaskID();
        } else if(isView) {
            taskID = allList.getViewList().get(lineNum-1).getTaskID();         
        } else {
            taskID = allList.getTaskList().get(lineNum-1).getTaskID();
        }
        return taskID;
    }
    
    public boolean isValidRank(String taskRank) {
        if(taskRank.matches(rankPattern)) {
            return true;
        } else {
            return false;
        }
    }
    
}
