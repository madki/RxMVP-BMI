package xyz.madki.rxmvp.bmi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

import rx.Observable;

public final class MainPresenter extends BasePresenter<MainPresenter.IView> {
  private static final float UNDER_WEIGHT_BMI_LIMIT = 18.5f;
  private static final float OVER_WEIGHT_BMI_LIMIT = 25f;


  @Override
  protected void onViewLoaded(IView view) {
    Observable<String> height$ = view.height$();
    Observable<String> weight$ = view.weight$();

    Observable<Float> heightFloat$ = height$.map(MainPresenter::stringToFloat);
    Observable<Float> weightFloat$ = weight$.map(MainPresenter::stringToFloat);

    Observable<Float> bmiFloat$ = Observable.combineLatest(
            heightFloat$, weightFloat$, MainPresenter::calculateBMI
    );
    Observable<String> bmiString$ = bmiFloat$.map(MainPresenter::bmiString);
    Observable<String> message$ = bmiFloat$.map(MainPresenter::getMessageFromBMI);
    addViewSubscriptions(
            bmiString$.subscribe(view::setBMI),
            message$.subscribe(view::setMessage)
    );
  }

  @NonNull
  static String bmiString(@Nullable Float bmi) {
    return bmi == null ? "BMI: ??" :
            String.format(Locale.getDefault(), "BMI: %.2f", bmi);
  }

  static boolean isDataValid(Float height, Float weight) {
    return !(height == null || weight == null || height == 0.0f);
  }

  @Nullable
  static Float calculateBMI(Float height, Float weight) {
    if (!isDataValid(height, weight)) return null;

    return weight * 10000 / (height * height);
  }

  @Nullable
  static Float stringToFloat(String s) {
    if (s == null || s.isEmpty()) return null;

    try {
      return Float.valueOf(s);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  static String getMessageFromBMI(Float bmi) {
    if (bmi == null) {
      return "Please enter your height and weight";
    }
    if (bmi <= UNDER_WEIGHT_BMI_LIMIT) {
      return "Dude! You need to eat more";
    }
    if (bmi >= OVER_WEIGHT_BMI_LIMIT) {
      return "You could use a little moving around";
    }
    return "You are normal. So boring";
  }

  public interface IView {
    Observable<String> height$();
    Observable<String> weight$();
    void setBMI(String bmiString);
    void setMessage(String message);
  }
}
