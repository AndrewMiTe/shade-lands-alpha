/*
 * MIT License
 *
 * Copyright (c) 2016 Andrew Michael Teller(https://github.com/AndrewMiTe)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package battle;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

/**
 * Participates in battles as a member of a squad. Can apply statuses to other
 * Fighter objects, or itself, by executing skills with the objective to defeat
 * all enemy fighters.
 * @author Andrew M. Teller(https://github.com/AndrewMiTe)
 */
public class Fighter implements Actor {

  /**
   * @see FighterBuilder#setName
   */
  private final String name;
  
  /**
   * @see FighterBuilder#setSquad
   */
  private final Squad squad;
  
  /**
   * @see Fighter#applyStatus
   */
  private final Set<Status> statusSet;
  
  /**
   * @see FighterBuilder#addSkill
   */
  private final List<Skill> skillList;
  
  /**
   * @see FighterBuilder#setCloseRange
   */
  private final int closeRange;

  /**
   * @see FighterBuilder#setIsAllyCase
   */
  private final BiPredicate<Fighter, Fighter> isAllyCase;
  
  /**
   * @see FighterBuilder#setIsEnemyCase
   */
  private final BiPredicate<Fighter, Fighter> isEnemyCase;
  
  /**
   * @see FighterBuilder#addListener
   */
  private final List<FighterHandler> listeners;
  
  /**
   * Initializes the object so that all internal field variables that can be
   * explicitly set are done so through the given parameters. See the {@link 
   * FighterBuilder} class which allows you to create Fighter objects using a
   * builder pattern.
   * @param name {@see FighterBuilder#setName}
   * @param squad {@see FighterBuilder#setSquad}
   * @param skillList {@see FighterBuilder#addSkill}
   * @param closeRange {@see FighterBuilder#setCloseRange}
   * @param isAllyCase {@see FighterBuilder#setIsAllyCase}
   * @param isEnemyCase {@see FighterBuilder#setIsEnemyCase}
   * @param listeners {@see FighterBuilder#addListener}
   */
  public Fighter(String name, Squad squad, List<Skill> skillList,
      int closeRange, BiPredicate<Fighter, Fighter> isAllyCase,
      BiPredicate<Fighter, Fighter> isEnemyCase,
      List<FighterHandler> listeners) {
    if (name == null) throw new NullPointerException("name: null");
    this.name = name;
    if (squad == null) throw new NullPointerException("squad: null");
    this.squad = squad;
    this.statusSet = new HashSet<>();
    if (skillList != null && skillList.contains(null)) {
      throw new NullPointerException("skill list: conatins null");
    }
    this.skillList = new ArrayList<>(skillList);
    if (closeRange < 0) throw new NullPointerException("close range: < 0");
    this.closeRange = closeRange;
    if (isAllyCase == null) throw new NullPointerException("Ally Case: null");
    this.isAllyCase = isAllyCase;
    if (isEnemyCase == null) throw new NullPointerException("Enemy Case: null");
    this.isEnemyCase = isEnemyCase;
    if (listeners != null && listeners.contains(null)) {
      throw new NullPointerException("listeners: conatins null");
    }
    this.listeners = new ArrayList<>(listeners);
  }
  
  /**
   * @param listener listener to be added.
   * @see FighterBuilder#addListener
   */
  public void addListener(FighterHandler listener) {
    if (listener == null) throw new NullPointerException("listener: null");
    listeners.add(listener);
  }
  
  /**
   * Removes a listener from the list of listeners.
   * @param listener the listener to be removed.
   * @return {@code true} if the listener was successfully removed.
   * @see FighterBuilder#addListener
   */
  public boolean removeListener(FighterHandler listener) {
    return listeners.remove(listener);
  }
  
  /**
   * @param skill skill to be added.
   * @see FighterBuilder#addSkill
   */
  public void addSkill(Skill skill) {
    if (skill == null) throw new NullPointerException("skill: null");
    skillList.add(skill);
  }
  
  /**
   * Removes a skill from the list of skills.
   * @param skill the skill to be removed.
   * @return {@code true} if the skill was successfully removed.
   * @see FighterBuilber#addSkill
   */
  public boolean removeSkill(Skill skill) {
    return skillList.remove(skill);
  }
  
  /**
   * Attempts to apply the given Status object to the fighter. Returns {@code
   * true} if the predicate for its application returned {@code true}.
   * @param status status to be applied.
   * @return {@code true} if the status was applied.
   */
  public boolean applyStatus(Status status) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  /**
   * Attempts to remove the given Status object from the fighter. Returns {@code
   * true} if the object was both found and if the predicate for its removal
   * returned {@code true}. This method will only remove the Status object
   * given.
   * @param status the status to be removed.
   * @return {@code true} if the status was removed.
   * @see #removeStatus(String name)
   */
  public boolean removeStatus(Status status) {
    if (statusSet.contains(status) && status.onRemove()) {
      return statusSet.remove(status);
    }
    return false;
  }

  /**
   * Attempts to remove from the fighter any Status object with a name property
   * matching the given String. Returns {@code true} if a match was both found
   * and if the predicate for its removal returned {@code true}.
   * @param name name of the status to be removed.
   * @return {@code true} if the status was removed.
   */
  public boolean removeStatus(String name) {
    return removeStatus(getStatus(name));
  }
  
  /**
   * Returns a Status object applied to the fighter with a matching name
   * property to the given Status.
   * @param status status with matching name of the status to be found.
   * @return matching status object. Null if no match was found.
   */
  public Status getStatus(Status status) {
    for (Status s: statusSet) {
      if (s.equals(status)) return s;
    }
    return null;
  }

  /**
   * Returns a Status object with a matching name to the given String if one has
   * been applied to the the fighter.
   * @param name name of the status to be found.
   * @return matching status object. Null if no match was found.
   */
  public Status getStatus(String name) {
    for (Status s : statusSet) {
      if (s.getName().equals(name)) return s;
    }
    return null;
  }

  /**
   * Returns {@code true} if the name property of the given status matches the
   * name of a status applied to the fighter.
   * @param status status with matching name of the status to be found.
   * @return {@code true} if a matching status is found.
   */
  public boolean hasStatus(Status status) {
    for (Status s : statusSet) {
      if (s.equals(status)) return true;
    }
    return false;
  }

  /**
   * Returns {@code true} if the given name of a status matches the name of a
   * status owned by the fighter.
   * @param name name of the status to be found.
   * @return {@code true} if a matching status is found.
   */
  public boolean hasStatus(String name) {
    for (Status s : statusSet) {
      if (s.getName().equals(name)) return true;
    }
    return false;
  }

  /**
   * Tells the fighter to attempt to execute the given skill. Returns true if
   * the skill or any sub-skill was successfully executed. The order in which
   * the given skill is executed is as such:<br><ol>
   * <li>The battlefield is checked for any valid targets that meet the skill's
   *    requirements. Valid targets must match the target criteria of the skill.
   *    The targets must also have all of the matching Status objects found in
   *    the Skill object's list of requirements. If no valid targets are found,
   *    then execution fails and this method returns false.</li>
   * <li>All Skill objects found in the list of sub-skills is executed
   *    recursively using this method. If all calls return false, then the Skill
   *    fails and this method returns false. If there are no sub-skills, the
   *    passed skill succeeds and applies its actions to valid targets. Up to
   *    the skill's maximum targets property can be affected by the skill,
   *    starting with those closest to the fighter.</li>
   * <li>If any but not all sub-skill executions return true, this Skill fails
   *    to apply its actions but returns true. If all sub-skill executions
   *    return true, this Skill succeeds and applies its actions to valid
   *    targets. Up to the skill's maximum targets property can be affected by
   *    the skill, starting with those closest to the fighter. This method would
   *    then return true. </li></ol>
   * Note that valid targets that meet the requirements are checked both before
   * and after sub-skills are executed. It is possible to create a skill that
   * can never apply its actions. This will be so if your sub-skills applies a
   * status that in turn removes a status that the primary skill requires, or if
   * it applies a status that defeats would-be targets.
   * @param skill the skill attempting to be executed.
   * @return {@code true} if the skill or any sub-Skill successfully executed.
   */
  public boolean executeSkill(Skill skill) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * @return name property of the fighter.
   * @see FighterBuilder#setName
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return close range property of the fighter.
   * @see FighterBuilder#setCloseRange
   */
  public int getCloseRange() {
    return closeRange;
  }
  
  /**
   * @return squad property of the fighter.
   * @see FighterBuilder#setSquad
   */
  public Squad getSquad() {
    return squad;
  }

  /**
   * Returns {@code true} if this fighter is an ally of the given fighter.
   * @param fighter fighter to test allied relationship with.
   * @return {@code true} if given fighter is an ally.
   */
  public boolean isAlly(Fighter fighter) {
    return isAllyCase.test(this, fighter);
  }

  /**
   * Returns {@code true} if this fighter is an enemy of the given fighter.
   * @param fighter fighter to test enemy relationship with.
   * @return {@code true} if given fighter is an enemy.
   */
  public boolean isEnemy(Fighter fighter) {
    return isEnemyCase.test(this, fighter);
  }

  /**
   * Returns {@code true} if the fighter has a status applied to it that stuns
   * it.
   * @return {@code true} if stunned.
   */
  public boolean isStunned() {
    for (Status nextStatus : statusSet) {
      if (nextStatus.isStunning()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Iterates through the list of Status object's and returns the duration of
   * the longest status that stuns.
   * @return duration the Unit is stunned.
   */
  private Duration getStunDuration() {
    Duration stunTime = Duration.ZERO;
    for (Status nextStatus : statusSet) {
      if (nextStatus.isStunning()) {
        if (stunTime.compareTo(nextStatus.getDuration()) < 0) {
          stunTime = nextStatus.getDuration();
        }
      }
    }
    return stunTime;
  }

  /**
   * Returns {@code true} if the fighter has a status applied to it that defeats
   * it.
   * @return {@code true} if defeated.
   */
  public boolean isDefeated() {
    for (Status nextStatus : statusSet) {
      if (nextStatus.isDefeating()) {
        return true;
      }
    }
    return false;
  }

}
