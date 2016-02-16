/**
 * Copyright 2012 
 * Kansas State University MACR Laboratory http://macr.cis.ksu.edu/
 * Department of Computing & Information Sciences
 *
 * See License.txt file for the license agreement. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package edu.ksu.cis.macr.aasis.agent.goals;

import edu.ksu.cis.macr.organization.model.identifiers.StringIdentifier;
import edu.ksu.cis.macr.organization.model.identifiers.UniqueIdentifier;

import java.lang.reflect.Field;

/**
 A singleton enum GoalParameters class mapping all goal parameters shown on the refined goal models to associated {@code UniqueIdentifier}s.  The string names must exactly match the variable names (not types) of the goal parameters shown
 on the refined goal models.  By convention, the unique identifier names should be exactly the same as the name
 strings.
 */
public enum AgentGoalParameters {
  INSTANCE;

  /**
   A generic set of agent guidelines.
   */
  public static final UniqueIdentifier agentGuidelines = StringIdentifier
          .getIdentifier("agentGuidelines");

   /**
   The connection guidelines for a single agent-to-agent connection.
   */
  public static final UniqueIdentifier connectionGuidelines = StringIdentifier.getIdentifier("connectionGuidelines");

  /**
   The goal parameters to configure the type of weather forecast.
   */
  public static final UniqueIdentifier forecastGuidelines =  StringIdentifier.getIdentifier("forecastGuidelines");


  @Override
  public String toString() {
    try {
      Field fields[] = Class.forName(
              this.getClass().getName()).getDeclaredFields();
      String s = "";
      for (Field field : fields) s.concat(field + " ");
      return s.concat(".");
    } catch (Exception e) {
      return "";
    }
  }

}
