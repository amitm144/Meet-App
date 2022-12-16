package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.data.split.SplitGroupBoundary;
import com.superapp.data.split.SplitGroupConverter;
import com.superapp.data.split.splitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SplitService {

        private SplitGroupConverter splitConverter;
        private splitEntity split;
        @Autowired
        public SplitService(SplitGroupConverter splitConverter) {
            this.splitConverter = splitConverter;
            this.split = new splitEntity();
        }

    public void invokeCommand(MiniAppCommandBoundary command) {
            String commandCase = command.getCommand();
            String groupId =DB.findbyID(command.getInvokedBy().getUserId()).gourpID; // TODO get users Group from DB
            SplitGroupBoundary group = new SplitGroupBoundary(groupId,command.getInvokedBy()); //String groupId, String superapp, UserEntity groupLeader, String avatar, String splitTitle

            switch (commandCase) {
                case "openNewSplitGroup":
                    this.split.openNewSplitGroup(this.splitConverter.toEntity());
                    break;
                case "openNewTrasnaction":
                    this.split.openNewTrasnaction(this.splitConverter.toEntity());
                    break;

                case "removeTrasnaction":
                    //TODO ADD removeTrasnacion
                    break;
                case "updateTransacion":
                    //TODO ADD updateTransacion
                    break;
                case "showDebt":
                    this.split.showDebt(this.splitConverter.toEntity());
                    break;
                case "payDebt":
                    this.split.payDebt(this.splitConverter.toEntity());
                    break;


            }
    }
}
