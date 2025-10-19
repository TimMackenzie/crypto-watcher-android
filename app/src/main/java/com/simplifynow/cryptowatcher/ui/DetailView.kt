package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.simplifynow.cryptowatcher.R
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
    item: BalanceCardDataSource.BalanceItem,
    prices: ImmutableMap<String, Double>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ETH addresses are too long to fit on one line unless text is much smaller
    val addressTextSize = if (item.address.length > 12)
        dimensionResource(id = R.dimen.text_small).value.sp
    else
        dimensionResource(id = R.dimen.text_largest).value.sp

    val textSizeMedium = dimensionResource(id = R.dimen.text_medium).value.sp
    val textSizeLarge = dimensionResource(id = R.dimen.text_large).value.sp

    val fiatPrice = parseNumericBalance(item.balance) * (prices[item.chainId] ?: 0.0)
    val df = DecimalFormat("#,##0.##")

    val textPadding = dimensionResource(id = R.dimen.padding_text)

    ModalBottomSheet(
        onDismissRequest = { onClose() },
        modifier = modifier,
        sheetState = sheetState
    ) {
        Column(
            modifier =
                Modifier
                    .padding(dimensionResource(id = R.dimen.padding_column))
                    .fillMaxWidth(),
        ) {
            Text(
                text = item.address,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Normal,
                fontSize = addressTextSize
            )
            Text(
                text = "\t Address on ${item.chainName}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = textSizeLarge
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer)))
            Text(
                text = "Balance:\n\t\t${parseNumericBalance(item.balance)} ${item.chainName}",
                modifier = Modifier.padding(top = textPadding, bottom = textPadding),
                fontWeight = FontWeight.Normal,
                fontSize = textSizeMedium
            )
            Text(
                text = "USD Value:\n\t\t$${df.format(fiatPrice)}",
                modifier = Modifier.padding(top = textPadding, bottom = textPadding),
                fontWeight = FontWeight.Normal,
                fontSize = textSizeMedium
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_double)))
        }
    }

    LaunchedEffect(Unit) {
        sheetState.show()
    }
}

fun parseNumericBalance(balanceText: String): Double =
    Regex("""\d+(\.\d+)?""")   // match an integer or decimal number
        .find(balanceText)
        ?.value
        ?.toDoubleOrNull()
        ?: 0.0

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewDetailView() {
    val item = BalanceCardDataSource.BalanceItem(
        0, "Wax", "wax","eosio", "123.4"
    )

    val prices = persistentMapOf(Pair("ethereum", 123.45), Pair("wax", 0.02))

    DetailView(item, prices, onClose = {})
}