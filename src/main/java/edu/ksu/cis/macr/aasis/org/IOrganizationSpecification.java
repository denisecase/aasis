/**
 *
 * Copyright 2012 Kansas State University MACR Laboratory
 * http://macr.cis.ksu.edu/ Department of Computing & Information Sciences
 *
 * See License.txt file for the license agreement.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package edu.ksu.cis.macr.aasis.org;

import java.io.File;

/**
 The {@code IOrganizationSpecification} defines the interface for defining the desired behavior for an organization.
 Each specification includes a set of specification files, including a: goalmodel, rolemodel, an agentfile, and
 environment file (optional).  It also includes the specified top goal, and includes a goal parameters initialize
 file, that provide the custom guidelines or parameters to use when starting the group.  For standard organizational
 functionality, see {@code IOrganization}.
 */
public interface IOrganizationSpecification {

  /**
   Checks the file exists.

   @param file - the file to check.
   @return - true if successful, false if not.
   */
  public boolean checkFile(final File file);

  /**
   Gets the agent file.

   @return the agentfile
   */
  public abstract String getAgentFile();


  // specification files...................................

  /**
   Sets the agent file.

   @param agentfile the relative path and file of the agent XML file, e.g. configs/TC01/selfH44/Agent.xml", containing
   information about the persona in the self agent.
   */
  public abstract void setAgentFile(final String agentfile);

  /**
   Gets the goal model file.

   @return a string containing relative path and file of the Goal Model file.
   */
  public abstract String getGoalFile();

  /**
   Sets the goal model file.

   @param goalfile the relative path and file of the Goal Model file (e.g. "configs/Scenario01/IGoalModel.goal").
   */
  public abstract void setGoalFile(final String goalfile);

  /**
   Gets the goal parameters files with the guidelines, e.g. configs/TC01/selfH44/Initialize.XML.

   @return - the relative path and file of the Initialize.XML file.
   */
  public String getGoalParametersFile();

  /**
   Sets the goal parameters file.

   @param goalparametersfile -  the relative path and file of the agent XML file, e.g. configs/TC01/selfH44/Agent.xml",
   containing information about the persona in the self agent.
   */
  public void setGoalParametersFile(final String goalparametersfile);

  /**
   Gets the object model file.

   @return the relative path and file of the organization XML file, e.g. configs/Scenario01/Environment.xml", containing
   information about objects in the SelfOrganization system.
   */
  public abstract String getObjectFile();

  /**
   Sets the object model file.

   @param objectfile - the objectfile to set
   */
  public abstract void setObjectFile(final String objectfile);

  /**
   Gets the role model file.

   @return a String containing the relative path and file of the Role Model file.
   */
  public abstract String getRoleFile();

  void setOrgModelFolder(String orgModelFolder);

  /**
   @param rolefile the relative path and file of the Role Model file (e.g. "configs/Scenario01/RoleModel.role").
   */
  public abstract void setRoleFile(String rolefile);

  /**
   Gets the top goal.

   @return the top goal in the Goal Model file.
   */
  public abstract String getTopGoal();


  // check files.................................

  /**
   Sets the top goal.

   @param topgoal - the name of the top Goal in the Goal Model file (e.g. "Succeed").
   */
  public abstract void setTopGoal(final String topgoal);

  /**
   Initialize the organization.
   */
  void initialize();

  /**
   Verifies the agent file.

   @return - true if successful, false if errors.
   */
  public boolean verifyAgentFile();
}
