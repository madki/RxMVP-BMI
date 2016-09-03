package xyz.madki.rxmvp.bmi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.subjects.PublishSubject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTests {

  @Mock MainPresenter.IView view;

  @Test
  public void bmiStringTest_validInputs() throws Exception {
    PublishSubject<String> mockHeight$ = PublishSubject.create();
    PublishSubject<String> mockWeight$ = PublishSubject.create();

    MainPresenter presenter = new MainPresenter();
    when(view.height$()).thenReturn(mockHeight$.asObservable());
    when(view.weight$()).thenReturn(mockWeight$.asObservable());

    presenter.takeView(view);

    mockHeight$.onNext("400");
    mockWeight$.onNext("16");

    verify(view, times(1)).setBMI("BMI: 1.00");
  }

  @Test
  public void calculateBMITest_validInputs() throws Exception {
    assertEquals(Float.valueOf(1.0f), MainPresenter.calculateBMI(400f, 16f));
  }
}
