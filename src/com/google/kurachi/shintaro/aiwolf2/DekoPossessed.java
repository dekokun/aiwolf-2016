package com.google.kurachi.shintaro.aiwolf2;

import org.aiwolf.client.base.player.AbstractPossessed;
import org.aiwolf.client.lib.TemplateTalkFactory;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.*;
import org.aiwolf.common.net.GameInfo;

import java.util.*;
import java.util.stream.Collectors;

public class DekoPossessed extends AbstractPossessed {
    boolean isComingOut = false;
    boolean isTodayToldFakeJudge = false;
    List<Judge> myToldFakeJudgeList = new ArrayList<>();
    List<Talk> todaysTalks = new ArrayList<>();
    Map<Role, List<Agent>> comingOutAgents = new HashMap<>();

    @Override
    public void finish() {
        isComingOut = false;
        myToldFakeJudgeList = new ArrayList<>();
        comingOutAgents = new HashMap<>();
    }

    @Override
    public String talk() {
        if(!isComingOut) {
            String comingOutTalk = TemplateTalkFactory.comingout(getMe(), getMyRole());
            isComingOut = true;
            return comingOutTalk;
        }
        if (!isTodayToldFakeJudge) {
            List<Agent> fakeJudgeCandidates = new ArrayList<>();

            fakeJudgeCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());
            getLatestDayGameInfo().getDay();
            fakeJudgeCandidates.remove(getMe());

            List<Agent> myToldFakeAgents = myToldFakeJudgeList.stream()
                    .map(judge -> judge.getAgent()).collect(Collectors.toList());

            long countToldFakeAgentAsWerewolf = myToldFakeJudgeList
                    .stream()
                    .filter(judge -> judge.getResult() == Species.WEREWOLF)
                    .count();

            Species fakeResult = Species.WEREWOLF;
            if(countToldFakeAgentAsWerewolf >= getGameSetting().getRoleNum(Role.WEREWOLF)) {
                fakeResult = Species.HUMAN;
            }

            fakeJudgeCandidates.removeAll(myToldFakeAgents);
            Agent fakeAgent = Util.randomSelect(fakeJudgeCandidates);
            isTodayToldFakeJudge = true;
            Judge fakeJudge = new Judge(getLatestDayGameInfo().getDay(), getMe(), fakeAgent, fakeResult);
            myToldFakeJudgeList.add(fakeJudge);
            return TemplateTalkFactory.divined(fakeAgent, fakeResult);
        }

        return Talk.OVER;
    }

    @Override
    public Agent vote() {
        List<Agent> voteCandidates = new ArrayList<>();
        voteCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());

        voteCandidates.remove(getMe());
        voteCandidates.removeAll(myToldFakeJudgeList.stream()
                .filter(judge -> judge.getResult() == Species.WEREWOLF)
                .map(judge -> judge.getAgent()).collect(Collectors.toList()));

        return Util.randomSelect(voteCandidates);
    }

    @Override
    public void dayStart() {
        isTodayToldFakeJudge = false;
        todaysTalks = new ArrayList<>();
    }

    @Override
    public void update(GameInfo gameInfo) {
        super.update(gameInfo);

        List<Talk> talkList = gameInfo.getTalkList();
        talkList.removeAll(todaysTalks);

        for(Talk talk: talkList) {
            Utterance utterance = new Utterance(talk.getContent());

            switch(utterance.getTopic()) {
                case COMINGOUT:
                    if (comingOutAgents.containsKey(utterance.getRole())) {
                        comingOutAgents.get(utterance.getRole()).add(utterance.getTarget());
                    } else {
                        comingOutAgents.put(utterance.getRole(), Arrays.asList(utterance.getTarget()));
                    }
                    break;
                case DIVINED:
                    break;
            }
            todaysTalks.add(talk);
        }
    }
}
