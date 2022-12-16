package com.superapp.logic.concreteServices;

import com.superapp.boundaries.command.MiniAppCommandBoundary;
import com.superapp.data.split.SplitGroupConverter;
import com.superapp.data.split.SplitGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SplitService {

        private SplitGroupConverter splitConverter;
        private SplitGroupEntity split;
        @Autowired
        public SplitService(SplitGroupConverter splitConverter) {
            this.splitConverter = splitConverter;
            this.split = new SplitGroupEntity();
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
