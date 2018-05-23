package com.e_gineering.maven.gitflowhelper;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * If the build is being executed from a FEATURE_OR_BUGFIX, DEVELOPMENT, HOTFIX or RELEASE branch, attach an artifact containing a list of
 * the attached artifacts. This list is then used for promoting artifacts from the stage repository to the release
 * repository. Or it can be used manually by the attach-deployed goal.
 *
 * If the build is being executed from the MASTER or SUPPORT branch, the artifacts from the stage repository
 * are downloaded and attached to the current build as if they were generated by the 'package' phase and checked by the
 * 'verify' phase (which should have happened as part of the build deploying to 'stage')
 */
@Mojo(name = "promote-master", defaultPhase = LifecyclePhase.INSTALL)
public class PromoteMasterMojo extends AbstractGitflowBasedRepositoryMojo {

    @Override
    protected void execute(final GitBranchType type, final String gitBranch, final String branchPattern) throws MojoExecutionException, MojoFailureException {
        switch (type) {
            case FEATURE_OR_BUGFIX_BRANCH:
            case DEVELOPMENT:
            case HOTFIX:
            case RELEASE: {
                // In order to use promote-master or attach-deployed, we need to build an artifactCatalog on deliverable branches.
                attachArtifactCatalog();
                break;
            }

            case SUPPORT:
            case MASTER: {
                getLog().info("Resolving & Reattaching existing artifacts from stageDeploymentRepository [" + stageDeploymentRepository + "]");

                attachExistingArtifacts(stageDeploymentRepository, true);

                break;
            }
        }
    }
}
