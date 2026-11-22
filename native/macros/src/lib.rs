use proc_macro::TokenStream;
use quote::{format_ident, quote};
use syn::{parse_macro_input, ItemFn, FnArg, Pat};

#[proc_macro_attribute]
pub fn validate_params(_attr: TokenStream, item: TokenStream) -> TokenStream {
    let mut input_fn = parse_macro_input!(item as ItemFn);

    for arg in &mut input_fn.sig.inputs {
        if let FnArg::Typed(pat_type) = arg {
            let Pat::Ident(_) = &*pat_type.pat else { continue; };

            pat_type.attrs.retain(|attr| {
                let attr_name = attr.path().get_ident().map(|i| i.to_string());

                match attr_name.as_deref() {
                    Some("max") | Some("min") | Some("nonzero") | Some("positive_only") => {
                        false
                    }
                    _ => true,
                }
            });
        }
    }

    TokenStream::from(quote! {
        #input_fn
    })
}

/// Export an i32-returning adapter for Nalim on AArch64, leaving the original C ABI intact.
#[proc_macro_attribute]
pub fn nalim_wide_return(_attr: TokenStream, item: TokenStream) -> TokenStream {
    let original = parse_macro_input!(item as ItemFn);
    let name = &original.sig.ident;
    let bridge = format_ident!("nalim_{}", name);
    let inputs = &original.sig.inputs;
    let mut args = Vec::with_capacity(inputs.len());
    for arg in inputs {
        if let FnArg::Typed(param) = arg {
            if let Pat::Ident(binding) = &*param.pat {
                args.push(&binding.ident);
                continue;
            }
        }
        return syn::Error::new_spanned(arg, "Nalim adapters require named parameters")
            .to_compile_error()
            .into();
    }

    quote! {
        #original

        #[no_mangle]
        pub extern "C" fn #bridge(#inputs) -> i32 {
            i32::from(#name(#(#args),*))
        }
    }.into()
}