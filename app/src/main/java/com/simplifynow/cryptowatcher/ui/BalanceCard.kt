package com.simplifynow.cryptowatcher.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.simplifynow.cryptowatcher.domain.BalanceCardDataSource

/**
 * Show a single wallet card with icon, address, and balance
 * @param item The data source to display
 * @param onClick the delete function to call when the card is clicked and confirmed for deletion
 */
@Composable
fun BalanceCard(item: BalanceCardDataSource.BalanceItem, onClick: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value) {
        DeleteCardConfirmation(onClick, showDialog)
    }

    Card(
        onClick = { showDialog.value = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = item.iconId),
                contentDescription = item.chainName,
                modifier = Modifier.padding(8.dp)
            )

            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = item.address,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = item.balance,
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
