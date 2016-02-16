/**
 *
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
package edu.ksu.cis.macr.aasis.agent.cc_message.custom;


import java.io.*;

/**
 * Provides a sample domain-specific message example.
 */
public class CustomMessageContent implements Serializable, ICustomMessageContent {
    private static final long serialVersionUID = 1L;
    private String sampleText = "";
    private int sampleValue = 1;
    private EquipmentStatus equipmentStatus = EquipmentStatus.AVAILABLE;


    public CustomMessageContent(String sampleText, int sampleValue, EquipmentStatus equipmentStatus) {
        this.sampleText = sampleText;
        this.sampleValue = sampleValue;
        this.equipmentStatus = equipmentStatus;
    }

    public EquipmentStatus getEquipmentStatus() {
        return equipmentStatus;
    }

    public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
    }

    public String getSampleText() {
        return sampleText;
    }

    public void setSampleText(String sampleText) {
        this.sampleText = sampleText;
    }

    public int getSampleValue() {
        return sampleValue;
    }

    public void setSampleValue(int sampleValue) {
        this.sampleValue = sampleValue;
    }

    @Override
    public String toString() {
        return "CustomMessageContent{" +
                "sampleText='" + sampleText + '\'' +
                ", sampleValue=" + sampleValue +
                ", equipmentStatus=" + equipmentStatus +
                '}';
    }

    /**
     * Deserialize the message.
     *
     * @param bytes - an array of bytes
     * @return the deserialized {@code Message}
     * @throws Exception - if an exception occurs.
     */
    @Override
    public Object deserialize(final byte[] bytes) throws Exception {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInput o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    /**
     * Serialize the message.
     *
     * @return a byte array with the contents.
     * @throws java.io.IOException - If an I/O error occurs.
     */
    @Override
    public byte[] serialize() throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutput o = new ObjectOutputStream(b)) {
                o.writeObject(this);
            }
            return b.toByteArray();
        }
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }
}
