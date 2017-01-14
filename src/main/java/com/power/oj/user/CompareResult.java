package com.power.oj.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CompareResult {
    private List<Integer> bothSolved;
    private List<Integer> firstSolved;
    private List<Integer> secondSolved;

    private List<Integer> bothFailed;
    private List<Integer> firstFailed;
    private List<Integer> secondFailed;

    public List<Integer> getBothSolved() {
        return bothSolved;
    }

    public void setBothSolved(Set<Integer> bothSolved) {
        this.bothSolved = new ArrayList<>(bothSolved);
        Collections.sort(this.bothSolved);
    }

    public List<Integer> getFirstSolved() {
        return firstSolved;
    }

    public void setFirstSolved(Set<Integer> firstSolved) {
        this.firstSolved = new ArrayList<>(firstSolved);
        Collections.sort(this.firstSolved);
    }

    public List<Integer> getSecondSolved() {
        return secondSolved;
    }

    public void setSecondSolved(Set<Integer> secondSolved) {
        this.secondSolved = new ArrayList<>(secondSolved);
        Collections.sort(this.secondSolved);
    }

    public List<Integer> getBothFailed() {
        return bothFailed;
    }

    public void setBothFailed(Set<Integer> bothFailed) {
        this.bothFailed = new ArrayList<>(bothFailed);
        Collections.sort(this.bothFailed);
    }

    public List<Integer> getFirstFailed() {
        return firstFailed;
    }

    public void setFirstFailed(Set<Integer> firstFailed) {
        this.firstFailed = new ArrayList<>(firstFailed);
        Collections.sort(this.firstFailed);
    }

    public List<Integer> getSecondFailed() {
        return secondFailed;
    }

    public void setSecondFailed(Set<Integer> secondFailed) {
        this.secondFailed = new ArrayList<>(secondFailed);
        Collections.sort(this.secondFailed);
    }
}
