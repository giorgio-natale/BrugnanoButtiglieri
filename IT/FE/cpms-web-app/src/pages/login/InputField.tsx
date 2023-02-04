import {Form} from "@themesberg/react-bootstrap";
import {useField} from "formik";
import * as React from "react";

interface Props {
  name: string;
  label: string | JSX.Element;
  autoCompleteEnabled?: boolean;
  placeholder: string;
  showValid?: boolean;
  type: string
}

export const InputField = (props: Props) => {
  const [field, meta, helpers] = useField(props);

  const {
    label,
    placeholder,
    type,
    showValid = true,
    autoCompleteEnabled = true,
    ...otherProps
  } = props;

  return (
    <Form.Group
      id={field.name}
      style={{display: "flex", flexDirection: "column", gap: "10px", alignItems: "flex-start"}}
    >
        <Form.Label className={`label`}>
          {label}
        </Form.Label>
      <Form.Control
        className="input"
        {...otherProps}
        {...field}
        isValid={showValid && meta.error === undefined && meta.touched}
        isInvalid={meta.error !== undefined && meta.touched}
        autoComplete={!autoCompleteEnabled ? "new-password" : undefined}
      />
      <Form.Control.Feedback className="error" type="invalid">{meta.error}</Form.Control.Feedback>
    </Form.Group>
  );

};