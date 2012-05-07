package org.xtext.example.mydsl.jvmmodel

import com.google.inject.Inject
import com.google.inject.Provider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.xtext.example.mydsl.myDsl.Statemachine

import static org.eclipse.xtext.common.types.JvmVisibility.*

class MyDslJvmModelInferrer extends AbstractModelInferrer {

  @Inject extension JvmTypesBuilder

 def dispatch void infer(Statemachine stm, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
   
   // create exactly one Java class per state machine
   acceptor.accept(stm.toClass(stm.className)).initializeLater [
     
     // add a field for each service annotated with @Inject
     members += stm.services.map[ service |
       service.toField(service.name, service.type) [
         annotations += service.toAnnotation(typeof(Inject))
       ]
     ]
     
     // generate a method for each state which has an action block
     members += stm.states.filter[action!=null].map[state |
       state.toMethod('do'+state.name.toFirstUpper, state.newTypeRef(Void::TYPE)) [
         visibility = PROTECTED
         
         // Associate the expression with the body of this method.
         body = state.action
       ]
     ]
     
     // generate a method containing the actual state machine code
     members += stm.toMethod("run", newTypeRef(Void::TYPE)) [
       
       // the run method shall be provided with an event provider Provider<String> 
       val eventProvider = stm.newTypeRef(typeof(Provider), stm.newTypeRef(typeof(String)))
       parameters += stm.toParameter("eventSource", eventProvider)
       
       // generate the body
       body = [append('''
         boolean executeActions = true;
         String currentState = "«stm.states.head.name»";
         String lastEvent = null;
         while (true) {
           «FOR state : stm.states»
             if (currentState.equals("«state.name»")) {
               «IF state.action != null»
                 if (executeActions) {
                   do«state.name.toFirstUpper»();
                   executeActions = false;
                 }
               «ENDIF»
               System.out.println("Your are now in state '«state.name»'. Possible events are [«
                 state.transitions.map[event.name].join(', ')»].");
               lastEvent = eventSource.get();
               «FOR t : state.transitions»
                 if ("«t.event.name»".equals(lastEvent)) {
                   currentState = "«t.state.name»";
                   executeActions = true;
                 }
               «ENDFOR»
             }
           «ENDFOR»
           «FOR resetEvent : stm.events.filter[resetEvent]»
             if ("«resetEvent.name»".equals(lastEvent)) {
               System.out.println("Resetting state machine.");
               currentState = "«stm.states.head.name»";
               executeActions = true;
             }
           «ENDFOR»
           
         }
       ''')]
     ]
   ]
 }
     
     def className(Statemachine statemachine) {
       val lastSegment = statemachine.eResource.URI.lastSegment
       lastSegment.substring(0, lastSegment.indexOf('.')) 
     }
}

