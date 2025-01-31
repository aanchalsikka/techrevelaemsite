package com.techrevel.core.workflows;

import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import org.apache.commons.lang3.StringUtils;

public class WorkflowUtils {
    /**
     * Get payload Path from WorkItem
     * @param workItem Current Workitem in the workflow
     * @return payload path
     */
    public static String getPayloadPath(WorkItem workItem) {
        if (null != workItem) {
            final WorkflowData data = workItem.getWorkflowData();

            if (null != data) {
                return (String) data.getPayload();
            }
        }
        return StringUtils.EMPTY;
    }
}
