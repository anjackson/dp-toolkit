/**
 * 
 */
package net.lovelycode.dpt.metadata.premis;

import gov.loc.standards.premis.v1_1.Agent;
import gov.loc.standards.premis.v1_1.Event;
import gov.loc.standards.premis.v1_1.Premis;
import gov.loc.standards.premis.v1_1.Rights;
import gov.loc.standards.premis.v1_1.Object;
import gov.loc.standards.premis.v1_1.Agent.AgentIdentifier;
import gov.loc.standards.premis.v1_1.Event.EventIdentifier;


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
        a.getAgentName().add("bob");
        p.getAgent().add(a);
        
        Object o = new Object();
        o.setObjectCategory(null);
        p.getObject().add(o);
        
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
