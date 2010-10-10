/**
 * 
 */
package net.lovelycode.dp.models.premis;

import gov.loc.standards.premis.v2_0.Agent;
import gov.loc.standards.premis.v2_0.Event;
import gov.loc.standards.premis.v2_0.OriginalName;
import gov.loc.standards.premis.v2_0.Premis;
import gov.loc.standards.premis.v2_0.Rights;
import gov.loc.standards.premis.v2_0.File;
import gov.loc.standards.premis.v2_0.AgentIdentifier;
import gov.loc.standards.premis.v2_0.EventIdentifier;


/**
 * @author AnJackson
 *
 */
public class PremisHelper {

    public void example() {
        //
        
        Premis p = new Premis();
        
        Agent a = new Agent();
        a.setAgentType(null);
        a.setVersion(null);
        a.setXmlID(null);
        AgentIdentifier ai = new AgentIdentifier();
        ai.setAgentIdentifierType(null);
        ai.setAgentIdentifierValue(null);
        a.getAgentIdentifier().add(ai);
        a.getAgentName().add("Agent Smith");
        p.getAgent().add(a);
        
        OriginalName on = new OriginalName();
        on.setTitle("Title");
        File f = new File();
        f.setOriginalName(on);
        p.getObject().add(f);
        
        Event e = new Event();
        e.setEventDateTime(null);
        e.setEventDetail(null);
        EventIdentifier ei = new EventIdentifier();
        ei.setEventIdentifierType(null);
        ei.setEventIdentifierValue(null);
        e.setEventIdentifier(ei);
        e.setEventType(null);
        p.getEvent().add(e);
        
        Rights r = new Rights();
        p.getRights().add(r);
        
        
        
    }
    
}
