export default interface InputProps {
  errors: string[];
  onChange(e: React.ChangeEvent<HTMLInputElement>): void;
}
