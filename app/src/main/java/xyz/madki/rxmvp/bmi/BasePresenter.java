package xyz.madki.rxmvp.bmi;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<V> {
  private V view;
  private CompositeSubscription viewSubscriptions;

  public final void takeView(V view) {
    this.view = view;
    viewSubscriptions = new CompositeSubscription();

    onViewLoaded(view);
  }

  protected abstract void onViewLoaded(V view);

  protected void onDropView(V view) {}

  public final void dropView(V view) {
    if (this.view != view) return;
    onDropView(view);

    if (areViewSubscriptionsActive()) {
      viewSubscriptions.unsubscribe();
      viewSubscriptions = null;
    }
    this.view = null;
  }

  protected void addViewSubscriptions(Subscription ...subscriptions) {
    if (!areViewSubscriptionsActive()) {
      throw new IllegalStateException("Cannot add view subscriptions. " +
              "No active subscription");
    }

    for (Subscription s: subscriptions) {
      viewSubscriptions.add(s);
    }
  }

  private boolean areViewSubscriptionsActive() {
    return viewSubscriptions != null && !viewSubscriptions.isUnsubscribed();
  }
}
