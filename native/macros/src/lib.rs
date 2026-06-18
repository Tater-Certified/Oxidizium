use proc_macro::TokenStream;
use quote::quote;
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