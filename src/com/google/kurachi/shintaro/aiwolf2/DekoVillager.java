package com.google.kurachi.shintaro.aiwolf2;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.base.player.AbstractVillager;
import org.aiwolf.common.data.Agent;

public class DekoVillager extends AbstractVillager {

    @Override
    public void dayStart() {
        // TODO Auto-generated method stub

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

}
