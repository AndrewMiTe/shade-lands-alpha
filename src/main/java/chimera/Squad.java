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

package chimera;

import java.util.HashSet;
import java.util.Set;

import core.Fighter;
import core.Team;

/**
 * Groups fighters together and assigns fighters in the same squad as being on
 * the same team.
 * 
 * @author Andrew M. Teller(https://github.com/AndrewMiTe)
 */
public class Squad implements Team {
  
  /**
   * The set of fighters in the squad.
   */
  private Set<Fighter> fighters;
  
  /**
   * Initializes an empty squad.
   */
  public Squad() {
    this.fighters = new HashSet<>();
  }
  
  /**
   * Removes the given fighter from its current squad and adds it to this one.
   * 
   * @param newFighter the fighter to be added.
   * @return {@code true} if the fighter was successfully assigned to the squad.
   */
  public boolean addFighter(Fighter newFighter) {
    Team oldTeam = newFighter.getTeam();
    if ((oldTeam != null) && (oldTeam instanceof Squad))
      ((Squad)newFighter.getTeam()).removeFighter(newFighter);
    if (fighters.add(newFighter)) return false;
    newFighter.setTeam(this);
    return true;
  }

  /**
   * Removes the given fighter from this squad and sets its team to {@code null}
   * if successful.
   * 
   * @param oldFighter the fighter to be removed.
   */
  private void removeFighter(Fighter oldFighter) {
    if (fighters.remove(oldFighter)) oldFighter.setTeam(null);
  }
  
}