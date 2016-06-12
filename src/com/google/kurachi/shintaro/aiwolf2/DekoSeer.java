package com.google.kurachi.shintaro.aiwolf2;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.base.player.AbstractSeer;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;

public class DekoSeer extends AbstractSeer {

    boolean isComingOut = false;
    List<Judge> myToldJudgeList = new ArrayList<>();

    @Override
    public Agent divine() {
        // TODO Auto-generated method stub
        List<Agent> divineCandidates = new ArrayList<>();

        divineCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());

        divineCandidates.remove(getMe());

        for(Judge judge: getMyJudgeList()) {
            if(divineCandidates.contains(judge.getTarget())) {
                divineCandidates.remove(judge.getTarget());
            }
        }

        if(divineCandidates.size() > 0) {
            return Util.randomSelect(divineCandidates);
        } else {
            return getMe();
        }
    }

    @Override
    public void finish() {
        isComingOut = false;
        myToldJudgeList = new ArrayList<>();
    }

    @Override
    public String talk() {
        if(!isComingOut) {
            for(Judge judge: getMyJudgeList()) {
                if(judge.getResult() == Species.WEREWOLF) {
                    String comingOutTalk = TemplateTalkFactory.comingout(getMe(), getMyRole());
                    isComingOut = true;
                    return comingOutTalk;
                }
            }
        } else {
            for(Judge judge: getMyJudgeList()) {
                if (!myToldJudgeList.contains(judge)) {
                    String resultTalk = TemplateTalkFactory.divined(judge.getTarget(), judge.getResult());
                    myToldJudgeList.add(judge);
                    return resultTalk;
                }
            }
        }

        return Talk.OVER;
    }

    @Override
    public Agent vote() {
        List<Agent> whiteAgent = new ArrayList<>();
        List<Agent> blackAgent = new ArrayList<>();

        for (Judge judge: getMyJudgeList()) {
            if(getLatestDayGameInfo().getAliveAgentList().contains(judge.getTarget())) {
                switch (judge.getResult()) {
                    case HUMAN:
                        whiteAgent.add(judge.getTarget());
                        break;
                    case WEREWOLF:
                        blackAgent.add(judge.getTarget());
                        break;
                }
            }
        }

        if(blackAgent.size() > 0) {
            return Util.randomSelect(blackAgent);
        }

        List<Agent> voteCandidates = new ArrayList<>();

        voteCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());
        voteCandidates.remove(getMe());
        voteCandidates.removeAll(whiteAgent);
        if (voteCandidates.size() <= 0) {
            voteCandidates = getLatestDayGameInfo().getAliveAgentList();
            voteCandidates.remove(getMe());
        }
        return Util.randomSelect(voteCandidates);
    }

}
