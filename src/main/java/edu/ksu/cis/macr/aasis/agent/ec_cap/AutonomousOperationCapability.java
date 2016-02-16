/**
 *
 * Copyright 2012 Denise Case Kansas State University MACR Laboratory
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
package edu.ksu.cis.macr.aasis.agent.ec_cap;


import edu.ksu.cis.macr.aasis.agent.persona.AbstractOrganizationCapability;
import edu.ksu.cis.macr.aasis.agent.persona.IOrganization;
import edu.ksu.cis.macr.aasis.agent.persona.IPersona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The {@code AutonomousOperationCapability} provides the ability to operate independent of any external organization,
 * fulfilling the objectives of either a purely autonomous agent, or an agent operating when completely disconnected from
 * the grid or any other prosumers.
 */
public class AutonomousOperationCapability extends AbstractOrganizationCapability {
    private static final Logger LOG = LoggerFactory.getLogger(AutonomousOperationCapability.class);
    private static final boolean debug = false;


    /**
     * Construct a new {@code AutonomousOperationCapability} instance.
     *
     * @param owner - the agent possessing this capability.
     * @param org   - the immediate organization in which this agent operates.
     */
    public AutonomousOperationCapability(final IPersona owner, final IOrganization org) {
        super(AutonomousOperationCapability.class, owner, org);
    }

    @Override
    public String toString() {
        return "AutonomousOperationCapability [no content yet=]";
    }

    @Override
    public void reset() {
    }

    @Override
    public double getFailure() {
        return 0;
    }

    @Override
    public Element toElement(final Document document) {
        final Element capability = super.toElement(document);
        return capability;
    }
}
