package mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.volunteer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Currency;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.MainActivity;
import mihaela.claudia.diosan.hapis_mihaelaclaudiadiosan.R;

public class PaymentActivity extends MainActivity {

    MaterialButton payBtn;
    CardForm cardForm;
    MaterialAlertDialogBuilder alertDialogBuilder;
    CurrencyEditText etInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setupPaymentForm();
    }

    private void setupPaymentForm(){
        etInput = (CurrencyEditText) findViewById(R.id.etInput);
        etInput.setCurrency("€");
        etInput.setDelimiter(false);
        etInput.setSpacing(true);
        etInput.setDecimals(true);
        etInput.setSeparator(".");


        cardForm = findViewById(R.id.card_form);
        payBtn = findViewById(R.id.button_pay);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation(getString(R.string.paymant_phone_explanation))
                .setup(PaymentActivity.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()){
                    showAlertDialog();
                }else {
                    showErrorToast(getString(R.string.payment_toast_fail));
                }
            }
        });
    }

    private void showAlertDialog(){
        alertDialogBuilder = new MaterialAlertDialogBuilder(PaymentActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.payment_confirm_title));
        alertDialogBuilder.setMessage(getString(R.string.payment_card__number) + cardForm.getCardNumber() + "\n" +
                getString(R.string.payment_card_expiration) + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                getString(R.string.payment_card_cvv) + cardForm.getCvv() + "\n" +
                getString(R.string.payment_postal_code) + cardForm.getPostalCode() + "\n" +
                getString(R.string.payment_phone_number) + cardForm.getMobileNumber());

        alertDialogBuilder.setPositiveButton( getString(R.string.payment_confirm_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(PaymentActivity.this,  getString(R.string.payment_toast_success), Toast.LENGTH_SHORT).show();
            }
        });

        alertDialogBuilder.setNegativeButton( getString(R.string.payment_cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void showErrorToast(String message){
        Toast toast = Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_LONG);
        View view =toast.getView();
        view.setBackgroundColor(Color.WHITE);
        TextView toastMessage =  toast.getView().findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.RED);
        toastMessage.setGravity(Gravity.CENTER);
        toastMessage.setTextSize(15);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.error_drawable, 0,0,0);
        toastMessage.setPadding(10,10,10,10);
        toast.show();
    }

}
