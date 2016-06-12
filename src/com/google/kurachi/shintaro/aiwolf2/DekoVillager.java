package com.google.kurachi.shintaro.aiwolf2;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.base.player.AbstractVillager;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;

public class DekoVillager extends AbstractVillager {

    List<Talk> todaysTalks = new ArrayList<>();
    @Override
    public void dayStart() {
        todaysTalks = new ArrayList<>();
    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub

    }

    @Override
    public String talk() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Agent vote() {
        List<Agent> voteCandidates = new ArrayList<>();
        voteCandidates.addAll(getLatestDayGameInfo().getAliveAgentList());

        voteCandidates.remove(getMe());

        return Util.randomSelect(voteCandidates);
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
                    break;
                case DIVINED:
                    break;
            }
            todaysTalks.add(talk);
        }
    }
}
