package xyz.madki.rxmvp.bmi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;

public class MainActivity extends AppCompatActivity implements MainPresenter.IView {
  private MainPresenter mainPresenter;

  private EditText etHeight;
  private EditText etWeight;
  private TextView tvBMI;
  private TextView tvMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    bindViews();
    mainPresenter = new MainPresenter();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mainPresenter.takeView(this);
  }

  @Override
  public Observable<String> height$() {
    return RxTextView.textChanges(etHeight).map(CharSequence::toString);
  }

  @Override
  public Observable<String> weight$() {
    return RxTextView.textChanges(etWeight).map(CharSequence::toString);
  }

  @Override
  public void setBMI(String bmiString) {
    tvBMI.setText(bmiString);
  }

  @Override
  public void setMessage(String message) {
    tvMessage.setText(message);
  }

  @Override
  protected void onPause() {
    mainPresenter.dropView(this);
    super.onPause();
  }

  private void bindViews() {
    etHeight = (EditText) findViewById(R.id.et_height);
    etWeight = (EditText) findViewById(R.id.et_weight);
    tvBMI = (TextView) findViewById(R.id.tv_bmi);
    tvMessage = (TextView) findViewById(R.id.tv_message);
  }
}
