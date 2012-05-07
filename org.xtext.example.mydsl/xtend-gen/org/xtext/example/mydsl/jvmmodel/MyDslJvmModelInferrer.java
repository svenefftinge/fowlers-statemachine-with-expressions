package org.xtext.example.mydsl.jvmmodel;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.common.types.JvmAnnotationReference;
import org.eclipse.xtext.common.types.JvmField;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.compiler.output.ITreeAppendable;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor.IPostIndexingInitializing;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.xtext.example.mydsl.myDsl.Event;
import org.xtext.example.mydsl.myDsl.Service;
import org.xtext.example.mydsl.myDsl.State;
import org.xtext.example.mydsl.myDsl.Statemachine;
import org.xtext.example.mydsl.myDsl.Transition;

@SuppressWarnings("all")
public class MyDslJvmModelInferrer extends AbstractModelInferrer {
  @Inject
  private JvmTypesBuilder _jvmTypesBuilder;
  
  protected void _infer(final Statemachine stm, final IJvmDeclaredTypeAcceptor acceptor, final boolean isPreIndexingPhase) {
    String _className = this.className(stm);
    JvmGenericType _class = this._jvmTypesBuilder.toClass(stm, _className);
    IPostIndexingInitializing<JvmGenericType> _accept = acceptor.<JvmGenericType>accept(_class);
    final Procedure1<JvmGenericType> _function = new Procedure1<JvmGenericType>() {
        public void apply(final JvmGenericType it) {
          EList<JvmMember> _members = it.getMembers();
          EList<Service> _services = stm.getServices();
          final Function1<Service,JvmField> _function = new Function1<Service,JvmField>() {
              public JvmField apply(final Service service) {
                String _name = service.getName();
                JvmTypeReference _type = service.getType();
                final Procedure1<JvmField> _function = new Procedure1<JvmField>() {
                    public void apply(final JvmField it) {
                      EList<JvmAnnotationReference> _annotations = it.getAnnotations();
                      JvmAnnotationReference _annotation = MyDslJvmModelInferrer.this._jvmTypesBuilder.toAnnotation(service, Inject.class);
                      MyDslJvmModelInferrer.this._jvmTypesBuilder.<JvmAnnotationReference>operator_add(_annotations, _annotation);
                    }
                  };
                JvmField _field = MyDslJvmModelInferrer.this._jvmTypesBuilder.toField(service, _name, _type, _function);
                return _field;
              }
            };
          List<JvmField> _map = ListExtensions.<Service, JvmField>map(_services, _function);
          MyDslJvmModelInferrer.this._jvmTypesBuilder.<JvmField>operator_add(_members, _map);
          EList<JvmMember> _members_1 = it.getMembers();
          EList<State> _states = stm.getStates();
          final Function1<State,Boolean> _function_1 = new Function1<State,Boolean>() {
              public Boolean apply(final State it) {
                XExpression _action = it.getAction();
                boolean _notEquals = (!Objects.equal(_action, null));
                return Boolean.valueOf(_notEquals);
              }
            };
          Iterable<State> _filter = IterableExtensions.<State>filter(_states, _function_1);
          final Function1<State,JvmOperation> _function_2 = new Function1<State,JvmOperation>() {
              public JvmOperation apply(final State state) {
                String _name = state.getName();
                String _firstUpper = StringExtensions.toFirstUpper(_name);
                String _plus = ("do" + _firstUpper);
                JvmTypeReference _newTypeRef = MyDslJvmModelInferrer.this._jvmTypesBuilder.newTypeRef(state, Void.TYPE);
                final Procedure1<JvmOperation> _function = new Procedure1<JvmOperation>() {
                    public void apply(final JvmOperation it) {
                      it.setVisibility(JvmVisibility.PROTECTED);
                      XExpression _action = state.getAction();
                      MyDslJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _action);
                    }
                  };
                JvmOperation _method = MyDslJvmModelInferrer.this._jvmTypesBuilder.toMethod(state, _plus, _newTypeRef, _function);
                return _method;
              }
            };
          Iterable<JvmOperation> _map_1 = IterableExtensions.<State, JvmOperation>map(_filter, _function_2);
          MyDslJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_1, _map_1);
          EList<JvmMember> _members_2 = it.getMembers();
          JvmTypeReference _newTypeRef = MyDslJvmModelInferrer.this._jvmTypesBuilder.newTypeRef(it, Void.TYPE);
          final Procedure1<JvmOperation> _function_3 = new Procedure1<JvmOperation>() {
              public void apply(final JvmOperation it) {
                JvmTypeReference _newTypeRef = MyDslJvmModelInferrer.this._jvmTypesBuilder.newTypeRef(stm, String.class);
                final JvmTypeReference eventProvider = MyDslJvmModelInferrer.this._jvmTypesBuilder.newTypeRef(stm, Provider.class, _newTypeRef);
                EList<JvmFormalParameter> _parameters = it.getParameters();
                JvmFormalParameter _parameter = MyDslJvmModelInferrer.this._jvmTypesBuilder.toParameter(stm, "eventSource", eventProvider);
                MyDslJvmModelInferrer.this._jvmTypesBuilder.<JvmFormalParameter>operator_add(_parameters, _parameter);
                final Procedure1<ITreeAppendable> _function = new Procedure1<ITreeAppendable>() {
                    public void apply(final ITreeAppendable it) {
                      StringConcatenation _builder = new StringConcatenation();
                      _builder.append("boolean executeActions = true;");
                      _builder.newLine();
                      _builder.append("String currentState = \"");
                      EList<State> _states = stm.getStates();
                      State _head = IterableExtensions.<State>head(_states);
                      String _name = _head.getName();
                      _builder.append(_name, "");
                      _builder.append("\";");
                      _builder.newLineIfNotEmpty();
                      _builder.append("String lastEvent = null;");
                      _builder.newLine();
                      _builder.append("while (true) {");
                      _builder.newLine();
                      {
                        EList<State> _states_1 = stm.getStates();
                        for(final State state : _states_1) {
                          _builder.append("  ");
                          _builder.append("if (currentState.equals(\"");
                          String _name_1 = state.getName();
                          _builder.append(_name_1, "  ");
                          _builder.append("\")) {");
                          _builder.newLineIfNotEmpty();
                          {
                            XExpression _action = state.getAction();
                            boolean _notEquals = (!Objects.equal(_action, null));
                            if (_notEquals) {
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("if (executeActions) {");
                              _builder.newLine();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("do");
                              String _name_2 = state.getName();
                              String _firstUpper = StringExtensions.toFirstUpper(_name_2);
                              _builder.append(_firstUpper, "      ");
                              _builder.append("();");
                              _builder.newLineIfNotEmpty();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("executeActions = false;");
                              _builder.newLine();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("}");
                              _builder.newLine();
                            }
                          }
                          _builder.append("  ");
                          _builder.append("  ");
                          _builder.append("System.out.println(\"Your are now in state \'");
                          String _name_3 = state.getName();
                          _builder.append(_name_3, "    ");
                          _builder.append("\'. Possible events are [");
                          EList<Transition> _transitions = state.getTransitions();
                          final Function1<Transition,String> _function = new Function1<Transition,String>() {
                              public String apply(final Transition it) {
                                Event _event = it.getEvent();
                                String _name = _event.getName();
                                return _name;
                              }
                            };
                          List<String> _map = ListExtensions.<Transition, String>map(_transitions, _function);
                          String _join = IterableExtensions.join(_map, ", ");
                          _builder.append(_join, "    ");
                          _builder.append("].\");");
                          _builder.newLineIfNotEmpty();
                          _builder.append("  ");
                          _builder.append("  ");
                          _builder.append("lastEvent = eventSource.get();");
                          _builder.newLine();
                          {
                            EList<Transition> _transitions_1 = state.getTransitions();
                            for(final Transition t : _transitions_1) {
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("if (\"");
                              Event _event = t.getEvent();
                              String _name_4 = _event.getName();
                              _builder.append(_name_4, "    ");
                              _builder.append("\".equals(lastEvent)) {");
                              _builder.newLineIfNotEmpty();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("currentState = \"");
                              State _state = t.getState();
                              String _name_5 = _state.getName();
                              _builder.append(_name_5, "      ");
                              _builder.append("\";");
                              _builder.newLineIfNotEmpty();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("executeActions = true;");
                              _builder.newLine();
                              _builder.append("  ");
                              _builder.append("  ");
                              _builder.append("}");
                              _builder.newLine();
                            }
                          }
                          _builder.append("  ");
                          _builder.append("}");
                          _builder.newLine();
                        }
                      }
                      {
                        EList<Event> _events = stm.getEvents();
                        final Function1<Event,Boolean> _function_1 = new Function1<Event,Boolean>() {
                            public Boolean apply(final Event it) {
                              boolean _isResetEvent = it.isResetEvent();
                              return Boolean.valueOf(_isResetEvent);
                            }
                          };
                        Iterable<Event> _filter = IterableExtensions.<Event>filter(_events, _function_1);
                        for(final Event resetEvent : _filter) {
                          _builder.append("  ");
                          _builder.append("if (\"");
                          String _name_6 = resetEvent.getName();
                          _builder.append(_name_6, "  ");
                          _builder.append("\".equals(lastEvent)) {");
                          _builder.newLineIfNotEmpty();
                          _builder.append("  ");
                          _builder.append("  ");
                          _builder.append("System.out.println(\"Resetting state machine.\");");
                          _builder.newLine();
                          _builder.append("  ");
                          _builder.append("  ");
                          _builder.append("currentState = \"");
                          EList<State> _states_2 = stm.getStates();
                          State _head_1 = IterableExtensions.<State>head(_states_2);
                          String _name_7 = _head_1.getName();
                          _builder.append(_name_7, "    ");
                          _builder.append("\";");
                          _builder.newLineIfNotEmpty();
                          _builder.append("  ");
                          _builder.append("  ");
                          _builder.append("executeActions = true;");
                          _builder.newLine();
                          _builder.append("  ");
                          _builder.append("}");
                          _builder.newLine();
                        }
                      }
                      _builder.append("  ");
                      _builder.newLine();
                      _builder.append("}");
                      _builder.newLine();
                      it.append(_builder);
                    }
                  };
                MyDslJvmModelInferrer.this._jvmTypesBuilder.setBody(it, _function);
              }
            };
          JvmOperation _method = MyDslJvmModelInferrer.this._jvmTypesBuilder.toMethod(stm, "run", _newTypeRef, _function_3);
          MyDslJvmModelInferrer.this._jvmTypesBuilder.<JvmOperation>operator_add(_members_2, _method);
        }
      };
    _accept.initializeLater(_function);
  }
  
  public String className(final Statemachine statemachine) {
    String _xblockexpression = null;
    {
      Resource _eResource = statemachine.eResource();
      URI _uRI = _eResource.getURI();
      final String lastSegment = _uRI.lastSegment();
      int _indexOf = lastSegment.indexOf(".");
      String _substring = lastSegment.substring(0, _indexOf);
      _xblockexpression = (_substring);
    }
    return _xblockexpression;
  }
  
  public void infer(final EObject stm, final IJvmDeclaredTypeAcceptor acceptor, final boolean isPreIndexingPhase) {
    if (stm instanceof Statemachine) {
      _infer((Statemachine)stm, acceptor, isPreIndexingPhase);
      return;
    } else if (stm != null) {
      _infer(stm, acceptor, isPreIndexingPhase);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(stm, acceptor, isPreIndexingPhase).toString());
    }
  }
}
