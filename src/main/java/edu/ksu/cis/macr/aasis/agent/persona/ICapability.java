/*
 * IExecutableCapability.java
 *
 * Created on Jan 23, 2006
 *
 * See License.txt file the license agreement.
 */
package edu.ksu.cis.macr.aasis.agent.persona;


import edu.ksu.cis.macr.obaa_pp.ec.IUnreliable;
import edu.ksu.cis.macr.obaa_pp.objects.IDisplayInformation;
import edu.ksu.cis.macr.obaa_pp.objects.IXMLFormattable;
import edu.ksu.cis.macr.organization.model.Capability;

/**
 The {@code ICapability} interface provides the basic functionality required for executable capabilities.  All are
 IXMLFormattable, but only some need to be IAttributable.

 @author Christopher Zhong, modified Denise Case
 @version $Revision: 1.1 $, $Date: 2009/03/04 15:40:29 $
*/
public interface ICapability extends Capability, IUnreliable, IXMLFormattable {

  /**
   Returns the {@code DisplayInformation} object containing the information for the {@code ICapability}.

   @param displayInformation the data display.
   */
  void populateCapabilitiesOfDisplayObject(
          IDisplayInformation displayInformation);

  /**
   Resets the {@code ICapability} and allows actions in the {@code ICapability} to be performed again for the new turn.
   */
  void reset();
}
