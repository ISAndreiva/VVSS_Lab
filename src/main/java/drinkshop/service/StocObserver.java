package drinkshop.service;

import drinkshop.domain.Stoc;

/**
 * Observer notified when a stock item drops below its minimum level.
 * Implements the Observer pattern (defect A06).
 */
public interface StocObserver {
    void onStocSubMinim(Stoc stoc);
}
