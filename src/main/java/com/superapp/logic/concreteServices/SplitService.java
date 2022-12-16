package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.data.split.splitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SplitService {

        private SplitConverter splitConverter;
        private splitEntity split;
        @Autowired
        public SplitService(SplitConverter splitConverter) {
            this.splitConverter = splitConverter;
            this.split = new splitEntity();
        }

    public void invokeCommand(MiniAppCommandBoundary command) {
            String commandCase = command.getCommand();
            switch (commandCase) {
                case "openNewTrasnaction":
                    this.splitConverter
                    break;

                case "payDebt":
                    break;

                case
            }
    }
}
