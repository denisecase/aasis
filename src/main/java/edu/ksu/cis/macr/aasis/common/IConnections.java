/**
 * Copyright 2014 Denise Case
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
package edu.ksu.cis.macr.aasis.common;

import edu.ksu.cis.macr.goal.model.InstanceParameters;
import edu.ksu.cis.macr.organization.model.identifiers.StringIdentifier;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Interface for connection guidelines describing all connections for which this agent is authorized.
 */
public interface IConnections extends Serializable {
    static IConnections extractConnections(InstanceParameters params, String strID) {
        return Objects.requireNonNull((IConnections) params
                .getValue(StringIdentifier.getIdentifier(strID)));
    }

    /**
     * Get a list of the ConnectionGuidelines this agent will attempt to establish and maintain.
     *
     * @return  - the list of all connections this agent is authorized to form
     */
    List<? extends IConnectionGuidelines> getListConnectionGuidelines();

    /**
     * Set the list of ConnectionGuidelines this agent should attempt to establish and maintain.
     *
     * @param listConnectionGuidelines - a list providing details for all external agent connections
     */
    void setListConnectionGuidelines(List<? extends IConnectionGuidelines> listConnectionGuidelines);

    String getTagName();

    void setTagName(String tagName);
}
