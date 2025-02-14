package com.vrondakis.zap.workflow;

import com.vrondakis.zap.ZapDriver;
import com.vrondakis.zap.ZapDriverController;
import hudson.FilePath;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.io.IOException;

/**
 * Executor for stopZap() function in Jenkins
 */
public class StopZapExecution extends DefaultStepExecutionImpl {
    public StopZapExecution(StepContext context) {
        super(context);
    }

    @Override
    public boolean start() {
        listener.getLogger().println("zap: Stopping Zap...");
        System.out.println("zap: Stopping Zap...");

        ZapDriver zap = ZapDriverController.getZapDriver(this.run, listener.getLogger());
        FilePath zapDir = zap.getZapDir();

        try {
            ZapDriverController.shutdownZap(this.run, listener.getLogger());
        } catch (Exception e) {
            listener.getLogger().println("zap: Failed to shutdown ZAP.");
        }

        if (zapDir != null) {
            try {
                listener.getLogger().println("zap: Deleting temp directory: " + zapDir.getRemote());
                zapDir.deleteRecursive();
            } catch (IOException | InterruptedException e) {
                listener.getLogger().println("zap: Failed to delete temp directory. " + e.getMessage());
            }
        }

        getContext().onSuccess(true);
        return true;
    }
}