package com.easyestate.android.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyestate.android.ui.theme.EasyEstateTheme
import java.text.NumberFormat
import java.util.Locale

data class InvoiceItem(
    val id: Int,
    var type: String = "Rent",
    var quantity: Int = 1,
    var amount: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvoiceScreen(
    onBack: () -> Unit,
    onSendInvoice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val invoiceItems = remember { mutableStateListOf(InvoiceItem(0, amount = 1200.0), InvoiceItem(1, type = "Water", amount = 50.0)) }
    var taxPercent by rememberSaveable { mutableStateOf("0") }

    val subtotal = invoiceItems.sumOf { it.quantity * it.amount }
    val taxAmount = subtotal * (taxPercent.toDoubleOrNull() ?: 0.0) / 100
    val total = subtotal + taxAmount

    val currencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Send Invoice",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp)) // Balance the title
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = onSendInvoice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Send Invoice", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                // Form fields for property, tenant, dates
            }

            item {
                ItemsSection(invoiceItems) {
                    invoiceItems.add(InvoiceItem(invoiceItems.size))
                }
            }

            item {
                OutlinedTextField(
                    value = taxPercent,
                    onValueChange = { taxPercent = it },
                    label = { Text("Tax (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                TotalsSummary(
                    subtotal = currencyFormat.format(subtotal),
                    tax = currencyFormat.format(taxAmount),
                    total = currencyFormat.format(total)
                )
            }
        }
    }
}

@Composable
private fun ItemsSection(
    items: List<InvoiceItem>,
    onAddItem: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Items", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEachIndexed { index, item ->
                InvoiceItemRow(
                    item = item,
                    onItemChange = { updatedItem ->
                        items[index].apply {
                            type = updatedItem.type
                            quantity = updatedItem.quantity
                            amount = updatedItem.amount
                        }
                    }
                )
            }
            Button(
                onClick = onAddItem,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Outlined.AddCircle, contentDescription = "Add Item", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Item", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InvoiceItemRow(
    item: InvoiceItem,
    onItemChange: (InvoiceItem) -> Unit
) {
    var type by remember { mutableStateOf(item.type) }
    var quantity by remember { mutableStateOf(item.quantity.toString()) }
    var amount by remember { mutableStateOf(item.amount.toString()) }

    LaunchedEffect(type, quantity, amount) {
        onItemChange(
            item.copy(
                type = type,
                quantity = quantity.toIntOrNull() ?: 1,
                amount = amount.toDoubleOrNull() ?: 0.0
            )
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.weight(2f)
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Rent", "Water", "Deposit", "Other").forEach { selection ->
                    DropdownMenuItem(text = { Text(selection) }, onClick = {
                        type = selection
                        expanded = false
                    })
                }
            }
        }
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.weight(2f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
        )
    }
}

@Composable
private fun TotalsSummary(
    subtotal: String,
    tax: String,
    total: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Subtotal", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(subtotal, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Tax", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(tax, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(total, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SendInvoiceScreenPreview() {
    EasyEstateTheme {
        SendInvoiceScreen(onBack = {}, onSendInvoice = {})
    }
}